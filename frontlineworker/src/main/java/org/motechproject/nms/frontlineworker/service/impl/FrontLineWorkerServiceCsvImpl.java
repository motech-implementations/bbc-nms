package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;
import org.motechproject.nms.frontlineworker.repository.FlwCsvRecordsDataService;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abhishek on 13/3/15.
 */
@Service("frontLineWorkerCsvService")
public class FrontLineWorkerServiceCsvImpl implements FrontLineWorkerCsvService {

    @Autowired
    FlwCsvRecordsDataService flwCsvRecordsDataService;

    @Override
    public FrontLineWorkerCsv createFrontLineWorkerCsv(FrontLineWorkerCsv frontLineWorkerCsv) {
        return flwCsvRecordsDataService.create(frontLineWorkerCsv);
    }

    @Override
    public FrontLineWorkerCsv findByIdInCsv(Long id) {
        return flwCsvRecordsDataService.findById(id);
    }

    @Override
    public void deleteFromCsv(FrontLineWorkerCsv frontLineWorkerCsv) {
        flwCsvRecordsDataService.delete(frontLineWorkerCsv);
    }

    @Override
    public List<FrontLineWorkerCsv> retrieveAllFromCsv() {
        return flwCsvRecordsDataService.retrieveAll();
    }
}
