package org.motechproject.nms.util.service;

import org.motechproject.nms.util.domain.BulkUploadStatus;

/**
 * Service interface for CRUD on simple repository records.
 */
public interface BulkUploadStatusService {

    void add(BulkUploadStatus record);

}