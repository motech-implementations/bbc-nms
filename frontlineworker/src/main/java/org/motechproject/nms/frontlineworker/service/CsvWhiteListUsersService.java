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

    public CsvWhiteListUsers findByIdInCsv(Long id);

    public void deleteFromCsv(CsvWhiteListUsers csvWhiteListUsers);

    public CsvWhiteListUsers createWhiteListUsersCsv(CsvWhiteListUsers csvWhiteListUsers);

    public List<CsvWhiteListUsers> retrieveAllFromCsv();

}
