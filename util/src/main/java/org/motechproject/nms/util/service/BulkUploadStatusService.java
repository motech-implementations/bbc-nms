package org.motechproject.nms.util.service;

import org.motechproject.nms.util.domain.BulkUploadStatus;

/**
 * Service interface for CRUD on simple repository records.
 */
public interface BulkUploadStatusService {

    /**
     * Method to create a new record of upload status.
     * This is invoked after csv processing is complete.
     *
     * @param bulkUploadStatus
     */
    void add(BulkUploadStatus bulkUploadStatus);

}
