package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.domain.CsvWhiteListUsers;
import org.motechproject.nms.frontlineworker.repository.CsvWhiteListUsersRecordDataService;
import org.motechproject.nms.frontlineworker.service.CsvWhiteListUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class acts as implementation class for the interface CsvWhiteListUsersService.
 * It uses CsvFrontLineWorkerRecordsDataService which further takes MotechDataService as
 * base class to performs the CRUD operations on CsvFrontLineWorker records.
 */
@Service("csvWhiteListUsersService")
public class CsvWhiteListUsersServiceImpl implements CsvWhiteListUsersService {

    public CsvWhiteListUsersRecordDataService csvWhiteListUsersRecordDataService;

    @Autowired
    public CsvWhiteListUsersServiceImpl(CsvWhiteListUsersRecordDataService csvWhiteListUsersRecordDataService) {
        this.csvWhiteListUsersRecordDataService = csvWhiteListUsersRecordDataService;
    }

    @Override
    public CsvWhiteListUsers findByIdInCsv(Long id) {
        return csvWhiteListUsersRecordDataService.findById(id);
    }

    @Override
    public void deleteFromCsv(CsvWhiteListUsers csvWhiteListUsers) {
        csvWhiteListUsersRecordDataService.delete(csvWhiteListUsers);
    }

}