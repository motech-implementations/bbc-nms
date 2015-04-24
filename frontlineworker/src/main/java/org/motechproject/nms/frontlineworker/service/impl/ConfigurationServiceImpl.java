package org.motechproject.nms.frontlineworker.service.impl;

/**
 * Created by abhishek on 24/2/15.
 */

import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.Configuration;
import org.motechproject.nms.frontlineworker.repository.ConfigurationDataService;
import org.motechproject.nms.frontlineworker.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of ConfigurationService
 */
@Service("configurationService")
public class ConfigurationServiceImpl implements ConfigurationService {


    private ConfigurationDataService configurationDataService;

    @Autowired
    public ConfigurationServiceImpl(ConfigurationDataService configurationDataService) {
        this.configurationDataService = configurationDataService;
    }

    /**
     * Create FrontLineWorker Service configuration
     *
     * @param configuration object to save
     */
    @Override
    public void createConfiguration(Configuration configuration) {
        configurationDataService.create(configuration);
    }

    /**
     * Create FrontLineWorker Service configuration
     *
     * @param configuration object to update
     */
    @Override
    public void updateConfiguration(Configuration configuration) {
        configurationDataService.update(configuration);
    }

    /**
     * get service configuration object
     *
     * @return object corresponding to service configuration
     */
    @Override
    public Configuration getConfiguration() {
        return configurationDataService.findRecordByIndex(ConfigurationConstants.CONFIGURATION_INDEX);
    }

    /**
     * Checks if the configuration is already present
     *
     * @return true if present, else false
     */
    @Override
    public Boolean isConfigurationPresent() {
        return (getConfiguration() != null);
    }
}
