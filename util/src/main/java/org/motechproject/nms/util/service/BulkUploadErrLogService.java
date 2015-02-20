package org.motechproject.nms.util.service;

import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;

public interface BulkUploadErrLogService {

    void writeBulkUploadErrLog(String logFileName, BulkUploadError erroneousRecord);

    void writeBulkUploadProcessingSummary(String userName, String logFileName, CsvProcessingSummary csvProcessingSummary);
}
