package org.motechproject.nms.kilkariobd.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Java program to generate MD5 checksum for files in Java. This Java example
 * uses core Java security package and Apache commons codec to generate MD5
 * checksum for a File.
 * @author Javin Paul
 */
public class Checksum {
    private static final Logger logger = Logger.getLogger(Checksum.class.getName());

    public static void main(String args[]) throws IOException {
        String file = "D:/Official/temp.txt";
        System.out.println("MD5 checksum for file using Java ["+checkSum(file)+"]");
    }

    /*
    * Calculate checksum of a File using MD5 algorithm
    */
    public static String checkSum(String path) {

        FileInputStream fis = null;
        String checksum = null;

        try {
            fis = new FileInputStream(path);
            MessageDigest md = MessageDigest.getInstance("MD5");

            //Using MessageDigest update() method to provide input
            byte[] buffer = new byte[8192];
            int numOfBytesRead;
            while( (numOfBytesRead = fis.read(buffer)) > 0){
                md.update(buffer, 0, numOfBytesRead);

            }
            byte[] hash = md.digest();
            checksum = new BigInteger(1, hash).toString(16); //don't use this, truncates leading zero
        } catch (IOException | NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return checksum;
    }



    /*
    * From Apache commons codec 1.4 md5() and md5Hex() method accepts InputStream as well.
    * If you are using lower version of Apache commons codec than you need to convert
    * InputStream to byte array before passing it to md5() or md5Hex() method.
    */

    //    public static String checkSumApacheCommons(String file){
    //
    //        String checksum = null;
    //
    //        try {
    //
    //            checksum = DigestUtils.md5Hex(new FileInputStream(file));
    //
    //        } catch (IOException ex) {
    //
    //            logger.log(Level.SEVERE, null, ex);
    //
    //        }
    //
    //        return checksum;
    //
    //    }




}