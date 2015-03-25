package org.motechproject.nms.kilkari.initializer;

import org.motechproject.nms.kilkari.domain.ActiveSubscriptionCount;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.service.ActiveSubscriptionCountService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * The purpose of this class is to perform initialization for Kilkari Service.
 * It creates and initializes the configuration parameters with default values, if not created already
 *
 */
@Component
public class Initializer {

    private Logger logger = LoggerFactory.getLogger(Initializer.class);
    
    public static final Long CONFIGURATION_INDEX = 1L;
    
    public static final Long ACTIVEUSER_INDEX = 1L;
    
    public static final Long DEFAULT_ACTIVEUSER_COUNT = 0L;

    public static final Integer DEFAULT_NUMBER_OF_MSG_PER_WEEK = 1;

    public static final Integer DEFAULT_ALLOWED_BENEFICIARY_COUNT = 9718577;

    public static final String DEFAULT_FRESH_OBD_SERVICE_ID = "1";

    public static final String DEFAULT_RETRY_DAY1_OBD_SERVICE_ID = "1";

    public static final String DEFAULT_RETRY_DAY2_OBD_SERVICE_ID = "1";

    public static final String DEFAULT_RETRY_DAY3_OBD_SERVICE_ID = "1";

    public static final String DEFAULT_FRESH_OBD_PRIORITY = "1";

    public static final String DEFAULT_RETRY_DAY1_OBD_PRIORITY = "1";

    public static final String DEFAULT_RETRY_DAY2_OBD_PRIORITY = "1";

    public static final String DEFAULT_RETRY_DAY3_OBD_PRIORITY = "1";

    public static final Integer DEFAULT_NATIONAL_LANGUAGE_LOCATION_CODE = 1;

    private ConfigurationService configurationService;
    
    private ActiveSubscriptionCountService activeSubscriptionCountService;


    @Autowired
    public Initializer(ConfigurationService configurationService, ActiveSubscriptionCountService activeSubscriptionCountService) {
        this.configurationService = configurationService;
        this.activeSubscriptionCountService = activeSubscriptionCountService;
    }

    /**
     * This Method is called post construction of Initalizer component and 
     * it creates the configuration entity in DB with default values, 
     * if not created already
     */
    @PostConstruct
    public void initializeConfiguration() {

        /*
         * Check if configuration is not present then create with default values
         */
        if (!configurationService.isConfigurationPresent()) {
            
            Configuration configuration = new Configuration();
            logger.info("Creating Configuration with default values");
            configuration.setIndex(CONFIGURATION_INDEX);
            configuration.setNumMsgPerWeek(DEFAULT_NUMBER_OF_MSG_PER_WEEK);
            configuration.setMaxAllowedActiveBeneficiaryCount(DEFAULT_ALLOWED_BENEFICIARY_COUNT);
            configuration.setFreshObdServiceId(DEFAULT_FRESH_OBD_SERVICE_ID);
            configuration.setRetryDay1ObdServiceId(DEFAULT_RETRY_DAY1_OBD_SERVICE_ID);
            configuration.setRetryDay2ObdServiceId(DEFAULT_RETRY_DAY2_OBD_SERVICE_ID);
            configuration.setRetryDay3ObdServiceId(DEFAULT_RETRY_DAY3_OBD_SERVICE_ID);
            configuration.setFreshObdPriority(DEFAULT_FRESH_OBD_PRIORITY);
            configuration.setRetryDay1ObdPriority(DEFAULT_RETRY_DAY1_OBD_PRIORITY);
            configuration.setRetryDay2ObdPriority(DEFAULT_RETRY_DAY2_OBD_PRIORITY);
            configuration.setRetryDay3ObdPriority(DEFAULT_RETRY_DAY3_OBD_PRIORITY);
            configuration.setNationalDefaultLanguageLocationCode(DEFAULT_NATIONAL_LANGUAGE_LOCATION_CODE);
            configurationService.createConfiguration(configuration);
            
        }
        
        if (!activeSubscriptionCountService.isActiveSubscriptionCountPresent()) {
            
            ActiveSubscriptionCount activeUser = new ActiveSubscriptionCount();
            logger.info("Creating ActiveUser with default values");
            activeUser.setIndex(ACTIVEUSER_INDEX);
            activeUser.setCount(DEFAULT_ACTIVEUSER_COUNT);
            activeSubscriptionCountService.create(activeUser); 
        }
    }

}
