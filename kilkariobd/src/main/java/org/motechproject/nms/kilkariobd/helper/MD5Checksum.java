package org.motechproject.nms.kilkariobd.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is use to generate MD5 checksum for files.
 */
public class MD5Checksum {
    private static final Logger logger = Logger.getLogger(MD5Checksum.class.getName());

    /*
    * Calculate checksum of a File using MD5 algorithm
    */
    public static String findChecksum(String filePath) {

        FileInputStream inputStream = null;
        String checksum = null;
        try {
            inputStream = new FileInputStream(filePath);
            MessageDigest msgDigest = MessageDigest.getInstance("MD5");

            //Using MessageDigest update() method to provide input
            byte[] buffer = new byte[8192];
            int numOfBytesRead;

            while( (numOfBytesRead = inputStream.read(buffer)) > 0){
                msgDigest.update(buffer, 0, numOfBytesRead);
            }

            byte[] hash = msgDigest.digest();
            checksum = new BigInteger(1, hash).toString(16); //don't use this, truncates leading zero
        } catch (IOException | NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return checksum;
    }
}