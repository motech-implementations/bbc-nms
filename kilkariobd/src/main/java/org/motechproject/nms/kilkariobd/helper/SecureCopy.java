package org.motechproject.nms.kilkariobd.helper;

import com.jcraft.jsch.*;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.domain.Configuration;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.nms.kilkariobd.settings.Settings;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class defines method to copy a file from remote location to local path and vice-versa
 */
public class SecureCopy {

    static Logger logger = LoggerFactory.getLogger(SecureCopy.class);

    /*
    Define Exception to be raised when error occurred while SCP.
    */
    private static class SecureCopyException extends Exception{
        private String errorMessage;
        SecureCopyException() {
            super();
        }

        SecureCopyException(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    /**
     * This method copies a file from local machine to remote machine using SCP
     * @param lFile name of the file to be copied.
     */
    public static void toRemote(String lFile, String rFile, Settings settings, Configuration configuration) {
        //Configuration configuration = configurationService.getConfiguration();
        int retry = Constants.FIRST_ATTEMPT;

        FileInputStream fileInputStream = null;
        while (retry++ <= 2) {
            try {
                /*
                 exec 'scp -t rFile' remotely
                  */
                String command = "scp -p -t " + rFile;
                Session session = getSession(configuration.getObdFileServerSshUsername(),
                        configuration.getObdFileServerIp(), settings.getSshLocalUsername(), settings);
                Channel channel = getChannel(session, command);

                OutputStream out = channel.getOutputStream();
                InputStream in = channel.getInputStream();
                channel.connect();

                checkAck(in);

                /*
                 send "C0644 fileSize filename", where filename should not include '/'
                  */
                long fileSize = (new File(lFile)).length();
                command = "C0644 " + fileSize + " ";

                if (lFile.lastIndexOf('/') > 0) {
                    command += lFile.substring(lFile.lastIndexOf('/') + 1);
                } else {
                    command += lFile;
                }
                command += "\n";
                out.write(command.getBytes());
                out.flush();

                checkAck(in);

                /*
                 send a content of lfile
                  */
                fileInputStream = new FileInputStream(lFile);
                byte[] buf = new byte[1024];
                while (true) {
                    int contentLength = fileInputStream.read(buf, 0, buf.length);
                    if (contentLength <= 0) {
                        break;
                    }
                    out.write(buf, 0, contentLength);
                }
                fileInputStream.close();
                fileInputStream = null;
                /*
                 send '\0'
                  */
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();
                checkAck(in);
                out.close();
                channel.disconnect();
            } catch (IOException | JSchException ex) {
                logger.error(ex.getMessage());
                try {
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                } catch (IOException ee) {
                    logger.error(ee.getMessage());
                }
            } catch (SecureCopyException scpEx) {
                logger.error(scpEx.getMessage());
            }
        }
    }

    /**
     * This method copies a file remote machine to local machine
     * @param lFile name of the file to be copied.
     */
    public static void fromRemote(String lFile, String rFile, Settings settings, Configuration configuration) throws IOException{
        //Configuration configuration = configurationService.getConfiguration();
        FileOutputStream fileOutputStream = null;

        int retry = 1;

        while(retry++ <= 3) {
            try {
                String prefix = null;
                if (new File(lFile).isDirectory()) {
                    prefix = lFile + File.separator;
                }

                /*
                 exec 'scp -f rFile' remotely
                  */
                String command = "scp -f " + rFile;
                Session session = getSession(configuration.getObdFileServerSshUsername(),
                        configuration.getObdFileServerIp(), settings.getSshLocalUsername(), settings);
                Channel channel = getChannel(session, command);

                /*
                 get I/O streams for remote scp
                  */
                OutputStream outputStream = channel.getOutputStream();
                InputStream inputStream = channel.getInputStream();
                channel.connect();

                byte[] buf = new byte[1024];

                /*
                 send '\0'
                  */
                buf[0] = 0;
                outputStream.write(buf, 0, 1);
                outputStream.flush();

                while (true) {
                    int charByte = checkAck(inputStream);
                    if (charByte != 'C') {
                        break;
                    }
                    /*
                     read '0644 '
                      */
                    inputStream.read(buf, 0, 5);

                    long fileSize = 0L;
                    while (true) {
                        if (inputStream.read(buf, 0, 1) < 0) { // error
                            break;
                        }
                        if (buf[0] == ' ') {
                            break;
                        }
                        fileSize = fileSize * 10L + (long) (buf[0] - '0');
                    }

                    String file = null;
                    for (int i = 0; ; i++) {
                        inputStream.read(buf, i, 1);
                        if (buf[i] == (byte) 0x0a) {
                            file = new String(buf, 0, i);
                            break;
                        }
                    }

                    /*
                     send '\0'
                      */
                    buf[0] = 0;
                    outputStream.write(buf, 0, 1);
                    outputStream.flush();

                    /*
                     read a content of lFile
                      */
                    fileOutputStream = new FileOutputStream(prefix == null ? lFile : prefix + file);
                    int bufferLength;
                    while (true) {
                        if (buf.length < fileSize) {
                            bufferLength = buf.length;
                        } else {
                            bufferLength = (int) fileSize;
                        }
                        bufferLength = inputStream.read(buf, 0, bufferLength);
                        if (bufferLength < 0) { // error
                            break;
                        }
                        fileOutputStream.write(buf, 0, bufferLength);
                        fileSize -= bufferLength;
                        if (fileSize == 0L) {
                            break;
                        }
                    }
                    fileOutputStream.close();
                    fileOutputStream = null;

                    checkAck(inputStream);

                    /*
                     send '\0'
                      */
                    buf[0] = 0;
                    outputStream.write(buf, 0, 1);
                    outputStream.flush();
                }

                session.disconnect();
            } catch (JSchException ex) {
                logger.error(ex.getMessage());
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (SecureCopyException scpEx) {
                logger.error(scpEx.getMessage());
            }
        }
    }

    private static byte[] readMyPrivateKeyFromFile(String fileName) throws SecureCopyException{
        try {
            Path path = Paths.get(fileName);
            return Files.readAllBytes(path);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            throw new SecureCopyException(String.format(
                    "Exception occurred while reading the private key from file %s", fileName));
        }
    }

    static int checkAck(InputStream inputStream) throws IOException, SecureCopyException{
        int nextByte = inputStream.read();
        /*
         nextByte may be 0 for success,1 for error,2 for fatal error,-1
        */

        /*
        If error is occurred while reading the byte
         */
        if(nextByte == 1 || nextByte == 2){
            StringBuffer stringBuffer = new StringBuffer();
            int charByte;
            do {
                charByte = inputStream.read();
                stringBuffer.append((char) charByte);
            }
            while(charByte!='\n');
            logger.error(stringBuffer.toString());
        }
        if(nextByte == 0) {
            return nextByte;
        } else {
            throw new SecureCopyException();
        }
    }

    private static Session getSession(String remoteUser, String remoteIp, String localUser, Settings settings) throws SecureCopyException{
        byte[] privateKey = readMyPrivateKeyFromFile(settings.getSshPrivateKeyFile());
        JSch jsch = new JSch();
        Session session = null;
        try {
            jsch.addIdentity(localUser, privateKey, null, null);
            session = jsch.getSession(remoteUser, remoteIp, 22);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
        } catch (JSchException ex) {
            logger.error(ex.getMessage());
            throw new SecureCopyException(ex.getMessage());
        }
        return session;
    }

    private static Channel getChannel(Session session, String command) throws SecureCopyException{
        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            if (channel != null) {
                ((ChannelExec) channel).setCommand(command);
            } else {
                throw new SecureCopyException("channel is null");
            }
        } catch (JSchException ex) {
            logger.error(ex.getMessage());
            throw new SecureCopyException(ex.getMessage());
        }
        return channel;
    }
}
