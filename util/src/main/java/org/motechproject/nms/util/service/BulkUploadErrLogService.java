package org.motechproject.nms.util.service;

public interface BulkUploadErrLogService {

    void writeBulkUploadErrLog(String logFileName, String erroneousRecord, String errorCode, String errorCause);

    void writeBulkUploadProcessingSummary(String logFileName, Integer numberOfSuccessfulRecords, Integer numberOfFailedRecords);
}
