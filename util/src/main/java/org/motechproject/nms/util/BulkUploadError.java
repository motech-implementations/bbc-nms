package org.motechproject.nms.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class models Bulk Upload erroneous record details
 * and the category and description of error
 */
public class BulkUploadError {

    private String recordDetails;
    private String errorCategory;
    private String errorDescription;

    public BulkUploadError() { }

    public BulkUploadError(String recordDetails, String errorCategory, String errorDescription) {
        this.recordDetails = recordDetails;
        this.errorCategory = errorCategory;
        this.errorDescription = errorDescription;
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

    /**
     * This method returns the name of Bulk upload error log file in the format
     * <csvFileName>_processing_<2007-12-14T15:30:00>.log
     * @param csvFileName Name of the csv file for which error log file name is to be created
     * @return Name of the error log file
     */
    public static String getBulkUploadErrLogFileName(String csvFileName) {

        DateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-ddThh:mm:ss");
        return (csvFileName + "_processing_" + logDateFormat.format(new Date()) + ".log");
    }
}
