package org.motechproject.nms.kilkariobd.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

/**
 * This class is use to generate MD5 checksum for files.
 */
public class MD5Checksum {
    private static Logger logger = LoggerFactory.getLogger(MD5Checksum.class);

    /**
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
            logger.error(ex.getMessage());
        } finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    logger.error(ex.getMessage());
                }
            }
        }
        return checksum;
    }
}