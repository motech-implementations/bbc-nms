package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;

import java.util.List;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * Its implementation uses the repository interface FrontLineWorkerCsvRecordsDataService whose base class
 * MotechDataService will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.
 */
public interface FrontLineWorkerCsvService {


    public FrontLineWorkerCsv createFrontLineWorkerCsv(FrontLineWorkerCsv frontLineWorkerCsv);

    public FrontLineWorkerCsv findByIdInCsv(Long id);

    public void deleteFromCsv(FrontLineWorkerCsv frontLineWorkerCsv);

    public List<FrontLineWorkerCsv> retrieveAllFromCsv();
}
