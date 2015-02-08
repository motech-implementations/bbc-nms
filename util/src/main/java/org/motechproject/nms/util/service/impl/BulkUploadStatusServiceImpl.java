package org.motechproject.nms.util.service.impl;

import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.repository.BulkUploadStatusDataService;
import org.motechproject.nms.util.service.BulkUploadStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link BulkUploadStatusService} interface. Uses
 * {@link org.motechproject.nms.util.repository.BulkUploadStatusDataService}
 * in order to persist records.
 */
@Service("bulkUploadStatusService")
public class BulkUploadStatusServiceImpl implements BulkUploadStatusService {

    private BulkUploadStatusDataService bulkUploadStatusDataService;

    private Logger logger = LoggerFactory.getLogger(BulkUploadStatusServiceImpl.class);

    @Autowired
    public BulkUploadStatusServiceImpl(BulkUploadStatusDataService bulkUploadStatusDataService) {
        this.bulkUploadStatusDataService = bulkUploadStatusDataService;
    }

    /**
     * Method to create a new record of upload status.
     * This is invoked after csv processing is complete.
     *
     * @param bulkUploadStatusRecord
     */
    @Override
    public void add(BulkUploadStatus bulkUploadStatusRecord) {
        logger.debug("Creating bulk upload completion status record for csv : {}", bulkUploadStatusRecord.getBulkUploadFileName());
        bulkUploadStatusDataService.create(bulkUploadStatusRecord);
    }
}
