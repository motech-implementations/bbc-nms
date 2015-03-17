package org.motechproject.nms.util.helper;

import org.joda.time.DateTime;
import org.motechproject.nms.util.constants.Constants;

/**
 * Utility class to provide static utility methods
 * that would be common for all the modules
 */
public final class NmsUtils {

    private NmsUtils() {

    }

    /**
     * This is a utility method to return the current timestamp
     *
     * @return current timestamp
     */
    public static DateTime getCurrentTimeStamp() {
        //DateFormat logDateFormat = new SimpleDateFormat("dd-MM-yy:HH:mm:SS");
        //String timeStamp = logDateFormat.format(new Date());
        //Date date = new Date();
        //Long time = date.getTime();
        //Timestamp timestamp = new Timestamp(time);

        DateTime dateTime = new DateTime();
        return dateTime;
    }
    
    public static String trimMsisdn(String msisdn) {
        int msisdnCsvLength = msisdn.length();
        
        if(msisdnCsvLength > Constants.MSISDN_LENGTH){
            msisdn = msisdn.substring(msisdnCsvLength-10, msisdnCsvLength);
        }
        return msisdn;
    }
}
