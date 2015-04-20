package org.motechproject.nms.frontlineworker.service.impl;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.scheduler.builder.CronJobSimpleExpressionBuilder;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Created by abhishek on 27/3/15.
 */
@Component
public class JobScheduler {
    private static final Logger LOG = LoggerFactory.getLogger(JobScheduler.class);

    private MotechSchedulerService schedulerService;

    @Autowired
    public JobScheduler(MotechSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    public void scheduleDeletionJob() {

        //loop here for calling job everyday
        CronSchedulableJob schedulableJob = getSchedulableDailyJob();
        schedulerService.safeScheduleJob(schedulableJob);
    }

    private CronSchedulableJob getSchedulableDailyJob() {
        Map<String, Object> eventParams = null;
        DateTime dateTime = new DateTime().withTimeAtStartOfDay();

        MotechEvent motechEvent = new MotechEvent(ConfigurationConstants.DELETION_EVENT_SUBJECT_SCHEDULER, eventParams);
        String cronJobExpression = new CronJobSimpleExpressionBuilder(new Time(dateTime.toLocalTime())).build();
        Date endDate = null;
        Date startDate = dateTime.toDate();
        return new CronSchedulableJob(motechEvent, cronJobExpression, startDate, endDate);


    }

}
