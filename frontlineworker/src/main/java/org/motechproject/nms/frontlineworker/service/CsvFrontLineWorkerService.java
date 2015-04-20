package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.CsvFrontLineWorker;

import java.util.List;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * Its implementation uses the repository interface CsvFrontLineWorkerRecordsDataService whose base class
 * MotechDataService will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.
 */
public interface CsvFrontLineWorkerService {


    public CsvFrontLineWorker createFrontLineWorkerCsv(CsvFrontLineWorker csvFrontLineWorker);

    public CsvFrontLineWorker findByIdInCsv(Long id);

    public void deleteFromCsv(CsvFrontLineWorker csvFrontLineWorker);

    public List<CsvFrontLineWorker> retrieveAllFromCsv();
}
