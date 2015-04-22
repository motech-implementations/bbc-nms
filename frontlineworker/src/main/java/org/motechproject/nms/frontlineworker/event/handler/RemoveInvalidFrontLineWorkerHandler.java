package org.motechproject.nms.frontlineworker.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.frontlineworker.service.RemoveInvalidRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by abhishek on 21/4/15.
 */
@Component
public class RemoveInvalidFrontLineWorkerHandler {
    private static Logger logger = LoggerFactory.getLogger(RemoveInvalidFrontLineWorkerHandler.class);

    //private static final String DATE_TIME_FORMAT = "dd-MM-yyyy";


    @Autowired
    RemoveInvalidRecords removeInvalidRecords;

    @Autowired
    FrontLineWorkerService frontLineWorkerService;

/*    @Autowired
    private JobTriggerService jobTriggerService;*/

    @MotechListener(subjects = "BATCH_JOB_TRIGGERED")
    public void handleEvent(MotechEvent motechEvent) {

        List<FrontLineWorker> frontLineWorkerListToBeDeleted = removeInvalidRecords.invalidFrontLineWorkerList();

        for(FrontLineWorker frontLineWorker: frontLineWorkerListToBeDeleted) {
            frontLineWorkerService.deleteFrontLineWorker(frontLineWorker);
        }


    }

/*
        DateTime parsedStartDate = parseDate(startDate);
        DateTime parsedEndDate = parseDate(endDate);
        LOGGER.debug("Parsed startDate is: " + parsedStartDate
                + " & Parsed endDate is: " + parsedEndDate);
        return mctsBeneficiarySyncService.syncBeneficiaryData(parsedStartDate,
                parsedEndDate);*/


    /**
     * method to convert from String to dateTime format
     *
     * @param dateString
     * @return
     */
/*    private static DateTime parseDate(String dateString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat
                .forPattern(DATE_TIME_FORMAT);
        return dateTimeFormatter.parseDateTime(dateString);
    }*/
}