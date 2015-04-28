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


    /**
     * This procedure is used to create a new csvFrontLineWorker record in database
     *
     * @param csvFrontLineWorker new record to be saved in database
     * @return new csvFrontLineWorker record saved in database
     */
    public CsvFrontLineWorker createFrontLineWorkerCsv(CsvFrontLineWorker csvFrontLineWorker);

    /**
     * This procedure is used to find the csvFrontLineWorker record in the csv to be uploaded
     *
     * @param id id of the record in csv
     * @return complete csvFrontLineWorker object fetched from csv to be uploaded
     */
    public CsvFrontLineWorker findByIdInCsv(Long id);

    /**
     * This procedure deletes the csvFrontLineWorker record from the uploaded csv file after CRUD operation is
     * performed on it
     *
     * @param csvFrontLineWorker record to be deleted from csv file
     */
    public void deleteFromCsv(CsvFrontLineWorker csvFrontLineWorker);

    /**
     * this procedure retrieves all the csvFrontLineWorker records from uploaded csv
     *
     * @return list of the csvFrontLineWorker records present in uploaded file
     */
    public List<CsvFrontLineWorker> retrieveAllFromCsv();
}
