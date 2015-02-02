package org.motechproject.nms.flw.service.impl;

import org.motechproject.nms.flw.domain.FrontLineWorker;
import org.motechproject.nms.flw.domain.FrontLineWorkerCsv;
import org.motechproject.nms.flw.repository.FlwCsvRecordsDataService;
import org.motechproject.nms.flw.repository.FlwRecordDataService;
import org.motechproject.nms.flw.service.FlwUploadRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by abhishek on 2/2/15.
 */
public class FlwUploadRecordServiceImpl implements FlwUploadRecordService {

    @Autowired
    FlwCsvRecordsDataService flwCsvRecordsDataService;

    @Autowired
    FlwRecordDataService flwRecordDataService;

    @Override
    public void add(FrontLineWorker frontLineWorker) {
        flwRecordDataService.create(frontLineWorker);

    }

    @Override
    public List<FrontLineWorkerCsv> retrieveAllRecords() {
         return flwCsvRecordsDataService.retrieveAll();
    }
}
