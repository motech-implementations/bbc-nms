package org.motechproject.nms.util.service;

import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;

/**
 * Service interface for writing erroneous bulk upload records
 * and bulk upload processing summary
 */
public interface BulkUploadErrLogService {

    /**
     * This method is used to write information corresponding to
     * erroneous records found during csv/bulk upload to db.
     * <p/>
     * Error Records contain the following information:
     * 1. Csv File Name
     * 2. Time of upload
     * 3. Erroneous record type
     * 4. Erroneous record details
     * 5. Error Category
     * 6. Error Description
     *
     * @param bulkUploadError BulkUploadError object containing
     *                        information of erroneous record
     */
    void writeBulkUploadErrLog(BulkUploadError bulkUploadError);

    /**
     * This method is used to write status of all the records after bulk upload is complete
     * <p/>
     * The bulk upload status contains:
     * 1. Number of Records Successfully uploaded
     * 2. Number of records failed to upload
     * 3. Name of csv uploaded
     * 4. Name of the user who uploaded the csv
     * 5. Time of upload
     *
     * @param bulkUploadStatus BulkUploadStatus object containing
     *                         summary of csv upload processing
     */
    void writeBulkUploadProcessingSummary(BulkUploadStatus bulkUploadStatus);
}
