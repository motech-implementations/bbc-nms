package org.motechproject.nms.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
public class BulkUploadError {

    private String recordDetails;
    private String errorCategory;
    private String errorDescription;
    private String userName;

    public BulkUploadError() { }

    public BulkUploadError(String recordDetails, String errorCategory, String errorDescription, String userName) {
        this.recordDetails = recordDetails;
        this.errorCategory = errorCategory;
        this.errorDescription = errorDescription;
        this.userName = userName;
    }

    public String getRecordDetails() {
        return recordDetails;
    }

    public void setRecordDetails(String recordDetails) {
        this.recordDetails = recordDetails;
    }

    public String getErrorCategory() {
        return errorCategory;
    }

    public void setErrorCategory(String errorCategory) {
        this.errorCategory = errorCategory;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method returns the name of Bulk upload error log file in the format
     * <csvFileName>_processing_<2007-12-14T15:30:00>.log
     * @param csvFileName Name of the csv file for which error log file name is to be created
     * @return Name of the error log file
     */
    public static String getBulkUploadErrLogFileName(String csvFileName) {

        DateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-ddThh:mm:ss");
        String logFileName = csvFileName + "_processing_" + logDateFormat.format(new Date()) + ".log";
        return logFileName;
    }
}
