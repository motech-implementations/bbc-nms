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

    /**
     * This procedure saves a new frontLineWorker record in database
     *
     * @param frontLineWorker record to be saved in database
     */
    public void createFrontLineWorker(FrontLineWorker frontLineWorker);

    /**
     * This procedure updates a frontLineWorker record in database
     *
     * @param frontLineWorker record to be updated in database
     */
    public void updateFrontLineWorker(FrontLineWorker frontLineWorker);

    /**
     * This procedure deleted a frontLineWorker record from database
     *
     * @param frontLineWorker record to be deleted from database
     */
    public void deleteFrontLineWorker(FrontLineWorker frontLineWorker);

    /**
     * This procedure is used to retrieve a frontLineWorker using contact number.If no record is found corresponding
     * to the combination, null is returned. If multiple records are found, then in case valid front line worker is
     * present in database, it is returned else first invalid record id returned.
     *
     * @param contactNo contact number for which corresponding frontLineWorker is to be returned.
     * @return frontLineWorker record corresponding to given contact number.
     */
    public FrontLineWorker getFlwBycontactNo(String contactNo);

    /**
     * This procedure is used to retrieve a frontLineWorker using flwId and state combination.If no record is found
     * corresponding to the combination, null is returned. If multiple records are found, then in case valid front line
     * worker is present in database, it is returned else first invalid record id returned.
     *
     * @param flwId     flwId of the frontLineWorker
     * @param stateCode stateCode of the frontLineWorker
     * @return frontLineWorker record corresponding to given flwId and state combination
     */
    public FrontLineWorker getFlwByFlwIdAndStateId(Long flwId, Long stateCode);

    /**
     * This procedure is used to find the FrontLineWorker record in the database using field "id"
     *
     * @param id id of the record in database
     * @return frontLineWorker record fetched from database
     */
    public FrontLineWorker findById(Long id);
}
