package org.motechproject.nms.frontlineworker;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.Configuration;
import org.motechproject.nms.frontlineworker.service.ConfigurationService;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * The purpose of this class is to perform initialization for Front Line Worker Service.
 * It creates and initializes the configuration parameters with default values, if not created already
 */
@Component
public class Initializer {

    private static final Logger LOG = LoggerFactory.getLogger(Initializer.class);

    private ConfigurationService configurationService;

    private MotechSchedulerService motechSchedulerService;

    public Initializer() {
    }

    @Autowired
    public Initializer(ConfigurationService configurationService, MotechSchedulerService motechSchedulerService) {
        this.configurationService = configurationService;
        this.motechSchedulerService = motechSchedulerService;
    }

    @PostConstruct
    public void initializeConfiguration() {

        Configuration configuration = new Configuration();

        //Check if configuration is not present then create with default values
        if (!configurationService.isConfigurationPresent()) {

            LOG.info("Creating Configuration with default values");
            configuration.setIndex(ConfigurationConstants.CONFIGURATION_INDEX);
            configuration.setPurgeDate(ConfigurationConstants.PURGE_DATE);
            configuration.setDeletionEventCronExpression(ConfigurationConstants.DELETION_EVENT_CRON_EXPRESSION);
            configurationService.createConfiguration(configuration);

        }

        Map<String, Object> parametersFrontLineWorkerDeletionPreparation = new HashMap<String, Object>() {
            {

                put(MotechSchedulerService.JOB_ID_KEY, ConfigurationConstants.FLW_DELETE_JOB);

            }
        };

        configuration = configurationService.getConfiguration();

        DateTime dateTime = new DateTime().withTimeAtStartOfDay();
        Date startDate = dateTime.toDate();
        Date endDate = null;

        MotechEvent motechEventForFlWDeletePreparation = new MotechEvent(ConfigurationConstants.FLW_DELETE_EVENT_SUBJECT, parametersFrontLineWorkerDeletionPreparation);

        CronSchedulableJob cronJobForObdPreparation = new CronSchedulableJob(motechEventForFlWDeletePreparation, configuration.getDeletionEventCronExpression(), startDate, endDate, true);

        motechSchedulerService.safeScheduleJob(cronJobForObdPreparation);
    }

}
