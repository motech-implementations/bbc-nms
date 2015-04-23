package org.motechproject.nms.batchscheduler.event.handler;

import org.motechproject.batch.exception.BatchException;
import org.motechproject.batch.service.JobTriggerService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by abhishek on 21/4/15.
 */
@Component
public class RemoveInvalidFrontLineWorkerHandler {
    private static Logger logger = LoggerFactory.getLogger(RemoveInvalidFrontLineWorkerHandler.class);

    //private static final String DATE_TIME_FORMAT = "dd-MM-yyyy";



    @Autowired
    private JobTriggerService jobTriggerService;

    @MotechListener(subjects = "BATCH_JOB_TRIGGERED")
    public void handleEvent(MotechEvent motechEvent) {
        logger.debug("Handling batch_Start event in batch module");
        String jobName = motechEvent.getParameters().get("Job_Name").toString();
        logger.debug("The job name is: " + jobName);
        try {
            jobTriggerService.triggerJob(jobName);
        } catch (BatchException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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