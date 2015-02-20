package org.motechproject.nms.util.service;

import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;

/**
 * Service interface for writing erroneous bulk upload records
 * and bulk upload processing summary
 */
public interface BulkUploadErrLogService {

    void writeBulkUploadErrLog(String bulkUploadFileName, String logFileName, BulkUploadError erroneousRecord);

    void writeBulkUploadProcessingSummary(String userName, String bulkUploadFileName, String logFileName, CsvProcessingSummary csvProcessingSummary);
}
