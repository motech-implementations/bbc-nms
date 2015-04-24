package org.motechproject.nms.frontlineworker;

import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.Configuration;
import org.motechproject.nms.frontlineworker.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * The purpose of this class is to perform initialization for Front Line Worker Service.
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
            configuration.setIndex(ConfigurationConstants.CONFIGURATION_INDEX);
            configuration.setPurgeDate(ConfigurationConstants.PURGE_DATE);
            configurationService.createConfiguration(configuration);

        }
    }

}
