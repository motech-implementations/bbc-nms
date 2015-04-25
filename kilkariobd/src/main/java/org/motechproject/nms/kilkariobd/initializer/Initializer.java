package org.motechproject.nms.kilkariobd.initializer;

import org.motechproject.event.MotechEvent;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.domain.Configuration;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * The purpose of this class is to perform initialization for Kilkariobd Service.
 * It creates and initializes the configuration parameters with default values, if not created already
 *
 */
@Component
public class Initializer {

    private Logger logger = LoggerFactory.getLogger(Initializer.class);

    public static final Long CONFIGURATION_INDEX = 1L;

    public static final String DEFAULT_FRESH_OBD_SERVICE_ID = "1";

    public static final String DEFAULT_RETRY_DAY1_OBD_SERVICE_ID = "1";

    public static final String DEFAULT_RETRY_DAY2_OBD_SERVICE_ID = "1";

    public static final String DEFAULT_RETRY_DAY3_OBD_SERVICE_ID = "1";

    public static final Integer DEFAULT_FRESH_OBD_PRIORITY = 1;

    public static final Integer DEFAULT_RETRY_DAY1_OBD_PRIORITY = 1;

    public static final Integer DEFAULT_RETRY_DAY2_OBD_PRIORITY = 1;

    public static final Integer DEFAULT_RETRY_DAY3_OBD_PRIORITY = 1;

    public static final String  DEFAULT_OBD_FILE_SERVER_IP = "127.0.0.1";

    public static final String DEFAULT_OBD_FILE_PATH_ON_SERVER = "/usr/share/nms";

    public static final String DEFAULT_OBD_FILE_SERVER_SSH_USERNAME = "nms";

    public static final String DEFAULT_OBD_IVR_URL = "http://10.10.10.10:8080/obdmanager";


    /*Triggers at 00:01am everyday */
    public static final String DEFAULT_OBD_CREATION_EVENT_CRON_EXPRESSION = "0 1 0 * * ?";

    /*Triggers at 8:00am everyday */
    public static final String DEFAULT_OBD_NOTIFICATION_EVENT_CRON_EXPRESSION = "0 0 8 * * ?";

    public static final Integer DEFAULT_RETRY_INTERVAL_FOR_OBD_PREPARATION_IN_MINS = 60;

    public static final Integer DEFAULT_MAX_OBD_PREPARATION_RETRY_COUNT = 3;

    private ConfigurationService configurationService;

    private MotechSchedulerService motechSchedulerService;

    @Autowired
    public Initializer(ConfigurationService configurationService, MotechSchedulerService motechSchedulerService) {
        this.configurationService = configurationService;
        this.motechSchedulerService= motechSchedulerService;
    }

    /**
     * This Method is called post construction of Initializer component and
     * it creates the configuration entity in DB with default values,
     * if not created already
     */
    @PostConstruct
    public void initializeConfiguration() {

        /*
         * Check if configuration is not present then create with default values
         */
        Configuration configuration = new Configuration();

        if (!configurationService.isConfigurationPresent()) {

            logger.info("Creating Configuration with default values");
            configuration.setIndex(CONFIGURATION_INDEX);
            configuration.setFreshObdServiceId(DEFAULT_FRESH_OBD_SERVICE_ID);
            configuration.setRetryDay1ObdServiceId(DEFAULT_RETRY_DAY1_OBD_SERVICE_ID);
            configuration.setRetryDay2ObdServiceId(DEFAULT_RETRY_DAY2_OBD_SERVICE_ID);
            configuration.setRetryDay3ObdServiceId(DEFAULT_RETRY_DAY3_OBD_SERVICE_ID);
            configuration.setFreshObdPriority(DEFAULT_FRESH_OBD_PRIORITY);
            configuration.setRetryDay1ObdPriority(DEFAULT_RETRY_DAY1_OBD_PRIORITY);
            configuration.setRetryDay2ObdPriority(DEFAULT_RETRY_DAY2_OBD_PRIORITY);
            configuration.setRetryDay3ObdPriority(DEFAULT_RETRY_DAY3_OBD_PRIORITY);
            configuration.setObdFileServerIp(DEFAULT_OBD_FILE_SERVER_IP);
            configuration.setObdFilePathOnServer(DEFAULT_OBD_FILE_PATH_ON_SERVER);
            configuration.setObdFileServerSshUsername(DEFAULT_OBD_FILE_SERVER_SSH_USERNAME);
            configuration.setObdIvrUrl(DEFAULT_OBD_IVR_URL);
            configuration.setObdCreationEventCronExpression(DEFAULT_OBD_CREATION_EVENT_CRON_EXPRESSION);
            configuration.setObdNotificationEventCronExpression(DEFAULT_OBD_NOTIFICATION_EVENT_CRON_EXPRESSION);
            configuration.setRetryIntervalForObdPreparationInMins(DEFAULT_RETRY_INTERVAL_FOR_OBD_PREPARATION_IN_MINS);
            configuration.setMaxObdPreparationRetryCount(DEFAULT_MAX_OBD_PREPARATION_RETRY_COUNT);
            configurationService.createConfiguration(configuration);

        }

        Map<String,Object> parametersObdPreparation = new HashMap<String,Object>() {{

            put(MotechSchedulerService.JOB_ID_KEY, Constants.PREPARE_OBD_TARGET_EVENT_JOB);

        }
        };

        Map<String,Object> parametersNotifyObdTarget = new HashMap<String, Object>() {{

            put(MotechSchedulerService.JOB_ID_KEY, Constants.NOTIFY_OBD_TARGET_EVENT_JOB);

        }
        };

        MotechEvent motechEventForObdPreparation = new MotechEvent(Constants.PREPARE_OBD_TARGET_EVENT_SUBJECT, parametersObdPreparation);

        MotechEvent motechEventToNotifyObdTarget = new MotechEvent(Constants.NOTIFY_OBD_TARGET_EVENT_SUBJECT, parametersNotifyObdTarget);

        CronSchedulableJob cronJobForObdPreparation = new CronSchedulableJob(motechEventForObdPreparation, configuration.getObdCreationEventCronExpression() , null, null, true);

        CronSchedulableJob cronJobToNotifyObdTarget = new CronSchedulableJob(motechEventToNotifyObdTarget, configuration.getObdNotificationEventCronExpression(), null, null, true);

        motechSchedulerService.safeScheduleJob(cronJobForObdPreparation);
        motechSchedulerService.safeScheduleJob(cronJobToNotifyObdTarget);

    }

}
