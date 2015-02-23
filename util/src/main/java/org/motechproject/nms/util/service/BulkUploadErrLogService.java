package org.motechproject.nms.util.service;

import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;

/**
 * Service interface for writing erroneous bulk upload records
 * and bulk upload processing summary
 */
public interface BulkUploadErrLogService {

    /**
     * This method is used to write logs for erroneous records
     * found during csv/bulk upload.
     * <p/>
     * Error logs are written to a separate log file
     * Error Logs contain the following information:
     * 1. Timestamp
     * 2. Erroneous record details
     * 3. Error Code
     * 4. Error Description
     *
     * @param logFileName     String containing the name of log file including path
     * @param bulkUploadError String describing another coding guideline
     */
    void writeBulkUploadErrLog(String logFileName, BulkUploadError bulkUploadError);

    /**
     * This method is used to write summary of all the records after bulk upload is complete
     * <p/>
     * The bulk upload summary is written to the file after bulk upload is complete
     * The bulk upload summary contains:
     * 1. Number of Records Successfully uploaded
     * 2. Number of records failed to upload
     *
     * @param logFileName          String containing name of log file including file path
     * @param csvProcessingSummary Number of records successfully uploaded during bulk upload
     */
    void writeBulkUploadProcessingSummary(String userName, String bulkUploadFileName, String logFileName, CsvProcessingSummary csvProcessingSummary);
}
