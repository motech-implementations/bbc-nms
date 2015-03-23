package org.motechproject.nms.mobileacademy.initializer;

import org.apache.log4j.Logger;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.Configuration;
import org.motechproject.nms.mobileacademy.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * The purpose of this class is to perform initialization for mobile academy
 * Service. It creates and initializes the configuration parameters with default
 * values, if not created already.
 *
 */
@Component
public class ConfigurationInitializer {

    private static final Logger LOGGER = Logger
            .getLogger(ConfigurationInitializer.class);

    private ConfigurationService configurationService;

    @Autowired
    public ConfigurationInitializer(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * This method is called after construction of ConfigurationInitializer
     * component and it creates the configuration entity in DB with default
     * values, if not created already
     */
    @PostConstruct
    public void initializeConfiguration() {
        /*
         * Check if configuration is not present then create with default values
         */
        if (!configurationService.isConfigurationPresent()) {
            Configuration configuration = new Configuration();
            LOGGER.info("Creating ma service configuration with default values");
            configuration
                    .setIndex(MobileAcademyConstants.CONFIG_DEFAULT_RECORD_INDEX);
            configuration
                    .setCappingType(MobileAcademyConstants.CONFIG_DEFAULT_CAPPING_TYPE);
            configuration
                    .setCourseQualifyingScore(MobileAcademyConstants.CONFIG_DEFAULT_COURSE_QUALIFYING_SCORE);
            configuration
                    .setDefaultLanguageLocationCode(MobileAcademyConstants.CONFIG_DEFAULT_LANGUAGE_LOCATION_CODE);
            configuration
                    .setMaxAllowedEndOfUsagePrompt(MobileAcademyConstants.CONFIG_DEFAULT_MAX_ALLOW_END_USAGE_PROMPT);
            configuration
                    .setNationalCapValue(MobileAcademyConstants.CONFIG_DEFAULT_NATIONAL_CAP_VALUE);
            configuration
                    .setSmsSenderAddress(MobileAcademyConstants.CONFIG_DEFAULT_SMS_SENDER_ADDRESS);
            configurationService.createConfiguration(configuration);
        }
    }

}
