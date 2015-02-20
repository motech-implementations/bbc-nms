package org.motechproject.nms.util.service.impl;

import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.repository.BulkUploadStatusDataService;
import org.motechproject.nms.util.service.BulkUploadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link BulkUploadStatusService} interface. Uses
 * {@link org.motechproject.nms.util.repository.BulkUploadStatusDataService}
 * in order to persist records.
 */
@Service("bulkUploadStatusService")
public class BulkUploadStatusServiceImpl implements BulkUploadStatusService {

    @Autowired
    private BulkUploadStatusDataService bulkUploadStatusDataService;

    @Override
    public void add(BulkUploadStatus bulkUploadStatusRecord) {
        bulkUploadStatusDataService.create(bulkUploadStatusRecord);
    }
}
