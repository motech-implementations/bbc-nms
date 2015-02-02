package org.motechproject.nms.flw.event;

/**
 * Created by abhishek on 2/2/15.
 */


import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.flw.domain.FrontLineWorker;
import org.motechproject.nms.flw.domain.FrontLineWorkerCsv;
import org.motechproject.nms.flw.service.FlwUploadRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Formatter;

@Component
public class FlwUploadHandler {

    @Autowired
    private FlwUploadRecordService flwUploadRecordService;

    @MotechListener(subjects = {"mds.crud.flw.FrontLineWorkerCsv.CREATE"})
    public void flwDataHandler(MotechEvent event)
    {
        List<FrontLineWorkerCsv> frontLineWorkerCsvList = flwUploadRecordService.retrieveAllRecords();

        for(Iterator<FrontLineWorkerCsv>itr=frontLineWorkerCsvList.iterator();itr.hasNext();)
        {
            FrontLineWorkerCsv flwCsvRecord = itr.next();

            FrontLineWorker flwRecord = getFlwData(flwCsvRecord);

            flwUploadRecordService.add(flwRecord);
        }
    }

    private FrontLineWorker getFlwData(FrontLineWorkerCsv flwCsvData)
    {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
                String dateInString = "17/02/2015";
                Formatter formatter= new Formatter();
                DateTime date = formatter.parse(dateInString);
                FrontLineWorker flwData = new FrontLineWorker(flwCsvData.getDistrictId(), Long.valueOf(flwCsvData.getStateId()).longValue(), flwCsvData.getContactNumber(), flwCsvData.getName(), flwCsvData.getType(),1, Long.valueOf(flwCsvData.getDistrictId()).longValue(), date,"true","true");

        return flwData;
    }

}


