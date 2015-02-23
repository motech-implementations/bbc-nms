package org.motechproject.nms.kilkari;

import static org.motechproject.nms.kilkari.constants.ConfigurationConstants.DEFAULT_48_WEEKS_PACK_MSGS_PER_WEEK;
import static org.motechproject.nms.kilkari.constants.ConfigurationConstants.DEFAULT_72_WEEKS_PACK_MSGS_PER_WEEK;
import static org.motechproject.nms.kilkari.constants.ConfigurationConstants.DEFAULT_ALLOWED_BENEFICIARY_COUNT;
import static org.motechproject.nms.kilkari.constants.ConfigurationConstants.DEFAULT_FRESH_OBD_PRIORITY;
import static org.motechproject.nms.kilkari.constants.ConfigurationConstants.DEFAULT_FRESH_OBD_SERVICE_ID;
import static org.motechproject.nms.kilkari.constants.ConfigurationConstants.DEFAULT_RETRY_DAY1_OBD_PRIORITY;
import static org.motechproject.nms.kilkari.constants.ConfigurationConstants.DEFAULT_RETRY_DAY1_OBD_SERVICE_ID;
import static org.motechproject.nms.kilkari.constants.ConfigurationConstants.DEFAULT_RETRY_DAY2_OBD_PRIORITY;
import static org.motechproject.nms.kilkari.constants.ConfigurationConstants.DEFAULT_RETRY_DAY2_OBD_SERVICE_ID;
import static org.motechproject.nms.kilkari.constants.ConfigurationConstants.DEFAULT_RETRY_DAY3_OBD_PRIORITY;
import static org.motechproject.nms.kilkari.constants.ConfigurationConstants.DEFAULT_RETRY_DAY3_OBD_SERVICE_ID;

import javax.annotation.PostConstruct;

import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




/**
 * The purpose of this class is to perform initialization for Kilkari Service.
 * It creates and initializes the configuration parameters with default values, if not created already
 *
 */
@Component
public class Initializer {

    private static final Logger LOG = LoggerFactory.getLogger(Initializer.class);


    @Autowired
    private ConfigurationService configurationService;

    @PostConstruct
    public void initializeConfiguration() {

        /*
         * Check if configuration is not present then create with default values
         */
        if (!configurationService.isConfigurationPresent()) {


            Configuration configuration = new Configuration();

            LOG.info("Creating Configuration with default values");

            configuration.setNmsKk48WeeksPackMsgsPerWeek(DEFAULT_48_WEEKS_PACK_MSGS_PER_WEEK);
            configuration.setNmsKk48WeeksPackMsgsPerWeek(DEFAULT_72_WEEKS_PACK_MSGS_PER_WEEK);
            configuration.setNmsKkMaxAllowedActiveBeneficiaryCount(DEFAULT_ALLOWED_BENEFICIARY_COUNT);
            configuration.setNmsKkFreshObdPriority(DEFAULT_FRESH_OBD_SERVICE_ID);
            configuration.setNmsKkRetryDay1ObdServiceId(DEFAULT_RETRY_DAY1_OBD_SERVICE_ID);
            configuration.setNmsKkRetryDay2ObdServiceId(DEFAULT_RETRY_DAY2_OBD_SERVICE_ID);
            configuration.setNmsKkRetryDay3ObdServiceId(DEFAULT_RETRY_DAY3_OBD_SERVICE_ID);
            configuration.setNmsKkFreshObdPriority(DEFAULT_FRESH_OBD_PRIORITY);
            configuration.setNmsKkRetryDay1ObdPriority(DEFAULT_RETRY_DAY1_OBD_PRIORITY);
            configuration.setNmsKkRetryDay2ObdPriority(DEFAULT_RETRY_DAY2_OBD_PRIORITY);
            configuration.setNmsKkRetryDay3ObdPriority(DEFAULT_RETRY_DAY3_OBD_PRIORITY);

            configurationService.createConfiguration(configuration);

        }
    }

}
