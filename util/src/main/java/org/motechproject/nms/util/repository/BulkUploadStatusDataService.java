package org.motechproject.nms.util.repository;

import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.util.domain.BulkUploadStatus;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface BulkUploadStatusDataService extends MotechDataService<BulkUploadStatus> {

}
