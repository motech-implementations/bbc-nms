package org.motechproject.nms.util;

/**
 * Created by root on 18/2/15.
 */
public class BulkUploadErrRecordDetails {
    private String recordDetails;
    private String errorCode;
    private String errorDescription;


    public BulkUploadErrRecordDetails() {
    }

    public BulkUploadErrRecordDetails(String recordDetails, String errorCode, String errorDescription) {
        this.recordDetails = recordDetails;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public String getRecordDetails() {
        return recordDetails;
    }

    public void setRecordDetails(String recordDetails) {
        this.recordDetails = recordDetails;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
