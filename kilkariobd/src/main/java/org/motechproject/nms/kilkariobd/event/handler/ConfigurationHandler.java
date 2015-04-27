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
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.motechproject.nms.kilkariobd.commons.Constants.CONFIGURATION_ID;
import static org.motechproject.nms.kilkariobd.commons.Constants.CONFIGURATION_UPDATE_EVENT;

/**
 * This class is the handler for event raised when Configuration entity is updated
 */
@Component
public class ConfigurationHandler {



    private ConfigurationService configurationService;
    private MotechSchedulerService motechSchedulerService;
    private static Logger logger = LoggerFactory.getLogger(ConfigurationHandler.class);

    @Autowired
    public ConfigurationHandler(ConfigurationService configurationService, MotechSchedulerService motechSchedulerService){

        this.configurationService = configurationService;
        this.motechSchedulerService = motechSchedulerService;
    }

    /**
     * This method handles the event raised on update done in Configuration entity.
     * @param motechEvent object for event
     */
    @MotechListener(subjects = CONFIGURATION_UPDATE_EVENT)
    public void configurationUpdate(MotechEvent motechEvent) {
        String obdCreationCronExpression;
        String obdNotificationCronExpression;
        String purgeRecordsCronExpression;
        Configuration configuration;

        logger.info("Event invoked [{}]" + CONFIGURATION_UPDATE_EVENT);

        Map<String, Object> parameters = motechEvent.getParameters();
        Long object_id = (Long)parameters.get(CONFIGURATION_ID);

        configuration = configurationService.getConfiguration(object_id);

        obdCreationCronExpression = configuration.getObdCreationEventCronExpression();
        obdNotificationCronExpression = configuration.getObdNotificationEventCronExpression();
        purgeRecordsCronExpression = configuration.getPurgeRecordsEventCronExpression();

        motechSchedulerService.rescheduleJob(Constants.PREPARE_OBD_TARGET_EVENT_SUBJECT,Constants.PREPARE_OBD_TARGET_EVENT_JOB, obdCreationCronExpression);
        motechSchedulerService.rescheduleJob(Constants.NOTIFY_OBD_TARGET_EVENT_SUBJECT, Constants.NOTIFY_OBD_TARGET_EVENT_JOB, obdNotificationCronExpression);
        motechSchedulerService.rescheduleJob(Constants.PURGE_RECORDS_EVENT_SUBJECT, Constants.PURGE_RECORDS_EVENT_JOB, purgeRecordsCronExpression);

        logger.info("Update event handling completed");
    }
}
