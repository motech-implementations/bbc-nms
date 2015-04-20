package org.motechproject.nms.kilkariobd.client;

import com.jcraft.jsch.*;
import com.jcraft.jsch.JSch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CopyFile {

    public static void main(String[] args) {
        try {
            fromRemote();
        } catch (JSchException ex) {
            ex.printStackTrace();
        }
    }

    private static void toRemote() throws JSchException {
        JSch jsch = new JSch();
        byte[] prvkey = readMyPrivateKeyFromFile("/home/ashish/.ssh/id_rsa");

        jsch.addIdentity("ashish", prvkey, null, null);
        Session session = null;
        Channel channel = null;

        String rfile = "/home/ashish";
        String lfile = "/home/ashish/priya/testmothercsv.csv";

        FileInputStream fis = null;
        try {
            // exec 'scp -t rfile' remotely
            String command = "scp -p -t " + rfile;
            session = getSession(jsch);
            channel = getChannel(session, command);
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();
            channel.connect();
            if (checkAck(in) != 0) {
                System.exit(0);
            }
            // send "C0644 filesize filename", where filename should not include
            // '/'
            long filesize = (new File(lfile)).length();
            command = "C0644 " + filesize + " ";
            if (lfile.lastIndexOf('/') > 0) {
                command += lfile.substring(lfile.lastIndexOf('/') + 1);
            } else {
                command += lfile;
            }
            command += "\n";
            out.write(command.getBytes());
            out.flush();
            if (checkAck(in) != 0) {
                System.exit(0);
            }

            // send a content of lfile
            fis = new FileInputStream(lfile);
            byte[] buf = new byte[1024];
            while (true) {
                int len = fis.read(buf, 0, buf.length);
                if (len <= 0)
                    break;
                out.write(buf, 0, len); // out.flush();
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

    private static void fromRemote() throws JSchException {
        JSch jsch = new JSch();
        byte[] prvkey = readMyPrivateKeyFromFile("/home/ashish/.ssh/id_rsa");

        jsch.addIdentity("ashish", prvkey, null, null);
        Session session = null;
        Channel channel = null;


        String rfile = "/home/ashish/query";
        String lfile = "/home/ashish";

        FileOutputStream fos = null;
        try {
            String prefix=null;
            if(new File(lfile).isDirectory()){
                prefix=lfile+File.separator;
            }

            // exec 'scp -f rfile' remotely
            String command = "scp -f " + rfile;
            session = jsch.getSession("ashish", "10.204.1.243", 22);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

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

                long filesize = 0L;
                while (true) {
                    if (in.read(buf, 0, 1) < 0) {
                        // error
                        break;
                    }
                    if (buf[0] == ' ') break;
                    filesize = filesize * 10L + (long) (buf[0] - '0');
                }

                String file = null;
                for (int i = 0; ; i++) {
                    in.read(buf, i, 1);
                    if (buf[i] == (byte) 0x0a) {
                        file = new String(buf, 0, i);
                        break;
                    }
                }

                //System.out.println("filesize="+filesize+", file="+file);

                // send '\0'
                buf[0] = 0;
                out.write(buf, 0, 1);
                out.flush();

                // read a content of lfile
                fos = new FileOutputStream(prefix == null ? lfile : prefix + file);
                int foo;
                while (true) {
                    if (buf.length < filesize) foo = buf.length;
                    else foo = (int) filesize;
                    foo = in.read(buf, 0, foo);
                    if (foo < 0) {
                        // error
                        break;
                    }
                    fos.write(buf, 0, foo);
                    filesize -= foo;
                    if (filesize == 0L) break;
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
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ee) {
                ee.printStackTrace();
            }
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

    private static Session getSession(JSch jsch) {
        Session session = null;
        try {
            session = jsch.getSession("ashish", "10.204.23.160", 22);
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
