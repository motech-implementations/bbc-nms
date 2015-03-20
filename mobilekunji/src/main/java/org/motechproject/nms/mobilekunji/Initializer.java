package org.motechproject.nms.mobilekunji;

import org.motechproject.nms.mobilekunji.domain.Configuration;
import org.motechproject.nms.mobilekunji.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.motechproject.nms.mobilekunji.constants.KunjiConstants.*;


/**
 * The purpose of this class is to perform initialization for MobileKunji Service.
 * It creates and initializes the configuration parameters with default values, if not created already
 */
@Component
public class Initializer {

    private static final Logger LOG = LoggerFactory.getLogger(Initializer.class);

    private ConfigurationService configurationService;


    public Initializer() {
    }

    @Autowired
    public Initializer(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @PostConstruct
    public void initializeConfiguration() {

        /*
         * Check if configuration is not present then create with default values
         */
        if (!configurationService.isConfigurationPresent()) {


            Configuration configuration = new Configuration();

            LOG.info("Creating Configuration with default values");
            configuration.setIndex(CONFIGURATION_INDEX);
            configuration.setCappingType(DEFAULT_CAPPING_TYPE);
            configuration.setMaxWelcomeMessage(DEFAULT_MAX_WELCOME_MESSAGE);
            configuration.setMaxEndofusageMessage(DEFAULT_MAX_END_OF_USAGE_MESSAGE);
            configuration.setNationalCapValue(DEFAULT_MAX_NATIONAL_CAPITAL_VALUE);
            configurationService.createConfiguration(configuration);

        }
    }

}
