package org.motechproject.nms.flw.event;

/**
 * Created by abhishek on 2/2/15.
 */


import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.flw.domain.FrontLineWorker;
import org.motechproject.nms.flw.domain.FrontLineWorkerCsv;
import org.motechproject.nms.flw.repository.FlwCsvRecordsDataService;
import org.motechproject.nms.flw.repository.FlwRecordDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class FlwUploadHandler {

    @Autowired
    private FlwRecordDataService flwRecordDataService;

    @Autowired
    private FlwCsvRecordsDataService flwCsvRecordsDataService;

    @MotechListener(subjects = { "mds.crud.flw.FrontLineWorkerCsv.CREATE" })
    public void flwDataHandler(MotechEvent event) {
        List<FrontLineWorkerCsv> frontLineWorkerCsvList = flwCsvRecordsDataService.retrieveAll();

        for (Iterator<FrontLineWorkerCsv> itr = frontLineWorkerCsvList.iterator(); itr.hasNext();) {

            FrontLineWorkerCsv flwCsvRecord = itr.next();

            FrontLineWorker flwRecord = getFlwData(flwCsvRecord);

            flwRecordDataService.create(flwRecord);
        }
    }

    private FrontLineWorker getFlwData(FrontLineWorkerCsv flwCsvData) {

                FrontLineWorker flwData = new FrontLineWorker(flwCsvData.getDistrictId(), Long.valueOf(flwCsvData.getStateId()).longValue(), flwCsvData.getContactNumber(), flwCsvData.getName(), flwCsvData.getType(), 1, Long.valueOf(flwCsvData.getDistrictId()).longValue(), "true", "true");

        return flwData;
    }

}


