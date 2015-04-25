package org.motechproject.nms.kilkariobd.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.domain.Configuration;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.motechproject.nms.kilkariobd.commons.Constants.CONFIGURATION_ID;
import static org.motechproject.nms.kilkariobd.commons.Constants.CONFIGURATION_UPDATE_EVENT;


public class ConfigurationHandler {

    public String cronExpression1;
    public String cronExpression2;
    public Configuration configuration;

    private ConfigurationService configurationService;
    private MotechSchedulerService motechSchedulerService;
    private static Logger logger = LoggerFactory.getLogger(ConfigurationHandler.class);

    @Autowired
    public ConfigurationHandler(ConfigurationService configurationService, MotechSchedulerService motechSchedulerService){

        this.configurationService = configurationService;
        this.motechSchedulerService = motechSchedulerService;
    }

    @MotechListener(subjects = CONFIGURATION_UPDATE_EVENT)
    public void configurationUpdate(MotechEvent motechEvent) {

        logger.info("Event invoked [{}]" + CONFIGURATION_UPDATE_EVENT);

        Map<String, Object> parameters = motechEvent.getParameters();
        Long object_id = (Long)parameters.get(CONFIGURATION_ID);

        configuration = configurationService.getConfiguration(object_id);

        cronExpression1 = configuration.getObdCreationEventCronExpression();
        cronExpression2 = configuration.getObdNotificationEventCronExpression();

        motechSchedulerService.rescheduleJob(Constants.PREPARE_OBD_TARGET_EVENT_SUBJECT,Constants.PREPARE_OBD_TARGET_EVENT_JOB, cronExpression1);
        motechSchedulerService.rescheduleJob(Constants.NOTIFY_OBD_TARGET_EVENT_SUBJECT, Constants.NOTIFY_OBD_TARGET_EVENT_JOB, cronExpression2);

        logger.info("Update event handling completed");
    }


}
