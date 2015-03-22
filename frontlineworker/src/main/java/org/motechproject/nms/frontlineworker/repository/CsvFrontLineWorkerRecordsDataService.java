package org.motechproject.nms.frontlineworker.repository;

import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.frontlineworker.domain.CsvFrontLineWorker;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.
 */
public interface CsvFrontLineWorkerRecordsDataService extends MotechDataService<CsvFrontLineWorker> {
}
