package org.motechproject.nms.util.service;

import org.motechproject.nms.util.BulkUploadErrRecordDetails;

public interface BulkUploadErrLogService {

    void writeBulkUploadErrLog(String logFileName, BulkUploadErrRecordDetails erroneousRecord);

    void writeBulkUploadProcessingSummary(String logFileName, Integer numberOfSuccessfulRecords, Integer numberOfFailedRecords);
}
