package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * Its implementation uses the repository interface FlwRecordsDataService whose base class
 * MotechDataService will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances. In this interface we
 * also declare lookups we may need to find record from Database.
 */
public interface FrontLineWorkerService {

    public void createFrontLineWorker(FrontLineWorker frontLineWorker);

    public void updateFrontLineWorker(FrontLineWorker frontLineWorker);

    public void deleteFrontLineWorker(FrontLineWorker frontLineWorker);

    public FrontLineWorker getFlwBycontactNo(String contactNo);

    public FrontLineWorker getFlwByFlwIdAndStateId(Long flwId, Long stateCode);

    public FrontLineWorker findById(Long id);
}
