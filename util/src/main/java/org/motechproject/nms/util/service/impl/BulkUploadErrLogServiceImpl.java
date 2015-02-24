package org.motechproject.nms.util.service.impl;

import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.repository.BulkUploadErrorDataService;
import org.motechproject.nms.util.repository.BulkUploadStatusDataService;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Simple implementation of the {@link BulkUploadErrLogService} interface.
 * <p/>
 * This class provides the api to log erroneous csv upload records in db table.
 * This api logs the erroneous record details along with error code and
 * error description
 */
@Service("bulkUploadErrLogService")
public class BulkUploadErrLogServiceImpl implements BulkUploadErrLogService {

    private BulkUploadStatusDataService bulkUploadStatusDataService;
    private BulkUploadErrorDataService bulkUploadErrorDataService;

    private Logger logger = LoggerFactory.getLogger(BulkUploadErrLogServiceImpl.class);

    @Autowired
    public BulkUploadErrLogServiceImpl(BulkUploadStatusDataService bulkUploadStatusDataService, BulkUploadErrorDataService bulkUploadErrorDataService) {
        this.bulkUploadStatusDataService = bulkUploadStatusDataService;
        this.bulkUploadErrorDataService = bulkUploadErrorDataService;
    }

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
    @Override
    public void writeBulkUploadErrLog(BulkUploadError bulkUploadError) {

        BulkUploadError bulkUploadErrorDeepCopy = bulkUploadError.createDeepCopy();

        //Adding the record to bulk upload error table
        bulkUploadErrorDataService.create(bulkUploadErrorDeepCopy);
        logger.info("Record added successfully for erroneous bulk upload record in {}", bulkUploadErrorDeepCopy.getRecordType());
    }

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
    @Override
    public void writeBulkUploadProcessingSummary(BulkUploadStatus bulkUploadStatus) {

        BulkUploadStatus bulkUploadStatusDeepCopy = bulkUploadStatus.createDeepCopy();

        //Adding the record to bulk upload status table
        bulkUploadStatusDataService.create(bulkUploadStatusDeepCopy);
        logger.info("Record added successfully for bulk upload completion status for csv : {}", bulkUploadStatus.getBulkUploadFileName());
    }
}
