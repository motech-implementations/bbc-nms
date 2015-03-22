package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.domain.CsvFrontLineWorker;
import org.motechproject.nms.frontlineworker.repository.CsvFrontLineWorkerRecordsDataService;
import org.motechproject.nms.frontlineworker.service.CsvFrontLineWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class acts as implementation class for the interface CsvFrontLineWorkerService.
 * It uses CsvFrontLineWorkerRecordsDataService which further takes MotechDataService as
 * base class to performs the CRUD operations on CsvFrontLineWorker records.
 */
@Service("csvFrontLineWorkerService")
public class CsvFrontLineWorkerServiceImpl implements CsvFrontLineWorkerService {


    public CsvFrontLineWorkerRecordsDataService csvFrontLineWorkerRecordsDataService;

    @Autowired
    public CsvFrontLineWorkerServiceImpl(CsvFrontLineWorkerRecordsDataService csvFrontLineWorkerRecordsDataService) {
        this.csvFrontLineWorkerRecordsDataService = csvFrontLineWorkerRecordsDataService;
    }

    @Override
    public CsvFrontLineWorker createFrontLineWorkerCsv(CsvFrontLineWorker csvFrontLineWorker) {
        return csvFrontLineWorkerRecordsDataService.create(csvFrontLineWorker);
    }

    @Override
    public CsvFrontLineWorker findByIdInCsv(Long id) {
        return csvFrontLineWorkerRecordsDataService.findById(id);
    }

    @Override
    public void deleteFromCsv(CsvFrontLineWorker csvFrontLineWorker) {
        csvFrontLineWorkerRecordsDataService.delete(csvFrontLineWorker);
    }

    @Override
    public List<CsvFrontLineWorker> retrieveAllFromCsv() {
        return csvFrontLineWorkerRecordsDataService.retrieveAll();
    }
}
