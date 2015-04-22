package org.motechproject.nms.kilkariobd.client;

import com.jcraft.jsch.*;
import com.jcraft.jsch.JSch;
import org.motechproject.nms.kilkariobd.domain.Configuration;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.nms.kilkariobd.settings.Settings;
import org.motechproject.server.config.SettingsFacade;
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

    @Autowired
    @Qualifier("kilkariObdSettings")
    private static SettingsFacade settingsFacade;

    @Autowired
    private static ConfigurationService configurationService;

    private static Settings settings = new Settings(settingsFacade);

    /**
     * This method copies a file from local machine to remote machine
     * @param fileName name of the file to be copied.
     */
    public static void toRemote(String fileName) {
        Configuration configuration = configurationService.getConfiguration();

        String rFile = configuration.getObdFilePathOnServer();
        String lFile = settings.getObdFileLocalPath() + "/" + fileName;

        FileInputStream fis = null;
        try {
            // exec 'scp -t rFile' remotely
            String command = "scp -p -t " + rFile;
            Session session = getSession(configuration.getObdFileServerSshUsername(),
                    configuration.getObdFileServerIp(), settings.getSshLocalUsername());
            Channel channel = getChannel(session, command);

            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();
            channel.connect();

            if (checkAck(in) != 0) {
                System.exit(0);
            }
            // send "C0644 filesize filename", where filename should not include
            // '/'
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

            if (checkAck(in) != 0) {
                System.exit(0);
            }

            // send a content of lfile
            fis = new FileInputStream(lFile);
            byte[] buf = new byte[1024];
            while (true) {
                int contentLength = fis.read(buf, 0, buf.length);
                if (contentLength <= 0)
                    break;
                out.write(buf, 0, contentLength); // out.flush();
            }
            fis.close();
            fis = null;
            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }
            out.close();
            channel.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }
            System.exit(0);
    }

    /**
     * This method copies a file remote machine to local machine
     * @param fileName name of the file to be copied.
     */
    public static void fromRemote(String fileName) throws IOException{
        Configuration configuration = configurationService.getConfiguration();

        FileOutputStream fos = null;

        String lFile = settings.getObdFileLocalPath();
        String rFile = configuration.getObdFilePathOnServer() + "/" + fileName;

        try {
            String prefix=null;
            if(new File(lFile).isDirectory()){
                prefix=lFile+File.separator;
            }

            // exec 'scp -f rFile' remotely
            String command = "scp -f " + rFile;
            Session session = getSession(configuration.getObdFileServerSshUsername(),
                    configuration.getObdFileServerIp(), settings.getSshLocalUsername());
            Channel channel = getChannel(session, command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();
            channel.connect();

            byte[] buf = new byte[1024];

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();

            while (true) {
                int c = checkAck(in);
                if (c != 'C') {
                    break;
                }
                // read '0644 '
                in.read(buf, 0, 5);

                long fileSize = 0L;
                while (true) {
                    if (in.read(buf, 0, 1) < 0)  { // error
                        break;
                    }
                    if (buf[0] == ' ') break;
                    fileSize = fileSize * 10L + (long) (buf[0] - '0');
                }

                String file = null;
                for (int i = 0; ; i++) {
                    in.read(buf, i, 1);
                    if (buf[i] == (byte) 0x0a) {
                        file = new String(buf, 0, i);
                        break;
                    }
                }

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();

                // read a content of lFile
                fos = new FileOutputStream(prefix == null ? lFile : prefix + file);
                int bufferLength;
                while (true) {
                    if (buf.length < fileSize) {
                        bufferLength = buf.length;
                    } else {
                        bufferLength = (int)fileSize;
                    }
                    bufferLength = in.read(buf, 0, bufferLength);
                    if (bufferLength < 0) { // error
                        break;
                    }
                    fos.write(buf, 0, bufferLength);
                    fileSize -= bufferLength;
                    if (fileSize == 0L) break;
                }
                fos.close();
                fos = null;

                if (checkAck(in) != 0) {
                    System.exit(0);
                }

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();
            }

            session.disconnect();

            System.exit(0);
        } catch (JSchException ex) {
            ex.printStackTrace();
            fos.close();
        }
    }

    private static byte[] readMyPrivateKeyFromFile(String aFileName) {
        try {
            Path path = Paths.get(aFileName);
            return Files.readAllBytes(path);
        } catch (IOException ex) {
            System.out.print(ex.toString());
        }
        return null;
    }

    static int checkAck(InputStream in) throws IOException{
        int b=in.read();
        // b may be 0 for success,
        // 1 for error,
        // 2 for fatal error,
        // -1
        if(b==0) return b;
        if(b==-1) return b;

        if(b==1 || b==2){
            StringBuffer sb=new StringBuffer();
            int c;
            do {
                c=in.read();
                sb.append((char)c);
            }
            while(c!='\n');
            if(b==1){ // error
                System.out.print(sb.toString());
            }
            if(b==2){ // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

    private static Session getSession(String remoteUser, String remoteIp, String localUser) {
        byte[] privateKey = readMyPrivateKeyFromFile(settings.getSshPrivateKeyFile());
        JSch jsch = new JSch();
        Session session = null;
        try {
            //todo : constant for username.
            jsch.addIdentity(localUser, privateKey, null, null);
            session = jsch.getSession(remoteUser, remoteIp, 22);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
        } catch (JSchException ex) {
            ex.printStackTrace();
        }
        return session;
    }

    private static Channel getChannel(Session session, String command) {
        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
        } catch (JSchException ex) {
            ex.printStackTrace();
        }
        return channel;
    }
}
