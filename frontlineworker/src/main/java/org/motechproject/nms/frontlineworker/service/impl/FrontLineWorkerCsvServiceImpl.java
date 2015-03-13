package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;
import org.motechproject.nms.frontlineworker.repository.FrontLineWorkerCsvRecordsDataService;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class acts as implementation class for the interface FrontLineWorkerCsvService.
 * It uses FrontLineWorkerCsvRecordsDataService which further takes MotechDataService as
 * base class to performs the CRUD operations on FrontLineWorkerCsv records.
 */
@Service("frontLineWorkerCsvService")
public class FrontLineWorkerCsvServiceImpl implements FrontLineWorkerCsvService {


    FrontLineWorkerCsvRecordsDataService frontLineWorkerCsvRecordsDataService;

    @Autowired
    public FrontLineWorkerCsvServiceImpl(FrontLineWorkerCsvRecordsDataService frontLineWorkerCsvRecordsDataService) {
        this.frontLineWorkerCsvRecordsDataService = frontLineWorkerCsvRecordsDataService;
    }

    @Override
    public FrontLineWorkerCsv createFrontLineWorkerCsv(FrontLineWorkerCsv frontLineWorkerCsv) {
        return frontLineWorkerCsvRecordsDataService.create(frontLineWorkerCsv);
    }

    @Override
    public FrontLineWorkerCsv findByIdInCsv(Long id) {
        return frontLineWorkerCsvRecordsDataService.findById(id);
    }

    @Override
    public void deleteFromCsv(FrontLineWorkerCsv frontLineWorkerCsv) {
        frontLineWorkerCsvRecordsDataService.delete(frontLineWorkerCsv);
    }

    @Override
    public List<FrontLineWorkerCsv> retrieveAllFromCsv() {
        return frontLineWorkerCsvRecordsDataService.retrieveAll();
    }
}
