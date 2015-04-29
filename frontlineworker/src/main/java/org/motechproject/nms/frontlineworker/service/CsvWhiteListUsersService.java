package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.CsvWhiteListUsers;

import java.util.List;


/**
 * Interface for repository that persists simple records and allows CRUD.
 * Its implementation uses the repository interface CsvWhiteListUsersRecordDataService whose base class
 * MotechDataService will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.
 */
public interface CsvWhiteListUsersService {

    /**
     * This procedure is used to find the csvWhiteListUser record in the csv to be uploaded
     *
     * @param id id of the record in csv
     * @return complete csvWhiteListUser object fetched from csv to be uploaded
     */
    public CsvWhiteListUsers findByIdInCsv(Long id);

    /**
     * This procedure deletes the csvWhiteListUsers record from the uploaded csv file after CRUD operation is
     * performed on it
     *
     * @param csvWhiteListUsers record to be deleted from csv file
     */
    public void deleteFromCsv(CsvWhiteListUsers csvWhiteListUsers);

    /**
     * This procedure is used to create a new csvWhiteListUsers record in database
     *
     * @param csvWhiteListUsers new record to be saved in database
     * @return new csvWhiteListUsers record saved in database
     */
    public CsvWhiteListUsers createWhiteListUsersCsv(CsvWhiteListUsers csvWhiteListUsers);

    /**
     * this procedure retrieves all the csvWhiteListUsers records from uploaded csv
     *
     * @return list of the csvWhiteListUsers records present in uploaded file
     */
    public List<CsvWhiteListUsers> retrieveAllFromCsv();

}
