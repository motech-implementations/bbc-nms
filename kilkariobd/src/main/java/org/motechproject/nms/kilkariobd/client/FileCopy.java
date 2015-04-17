package org.motechproject.nms.kilkariobd.client;

import com.jcraft.jsch.*;
import com.jcraft.jsch.JSch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileCopy {

    public static void main(String[] args) {
        try {
            execute();
        } catch (JSchException ex) {
            ex.printStackTrace();
        }
    }

    private static void execute() throws JSchException {
        JSch jsch = new JSch();
        byte[] prvkey = readMyPrivateKeyFromFile("/home/ashish/.ssh/id_rsa");

        jsch.addIdentity(
                "ashish",    // String userName
                prvkey,          // byte[] privateKey
                null,            // byte[] publicKey
                null  // byte[] passPhrase
        );
        Session session = null;
        Channel channel = null;

        try {
            session = jsch.getSession("ashish", "10.204.1.243", 22);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            String command = "scp ashish@10.204.1.243:/home/ashish/query.csv /home/ashish";
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            channel.connect();
            String str = Checksum.checkSum("/home/ashish/priya/testmothercsv.csv");
            System.out.print(str);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
         finally {
            if (channel != null ) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }

        }
            System.exit(0);
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
}
