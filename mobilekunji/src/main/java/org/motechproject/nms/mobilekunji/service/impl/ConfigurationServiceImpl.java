package org.motechproject.nms.mobilekunji.service.impl;

/**
 * Created by abhishek on 24/2/15.
 */

import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.mobilekunji.domain.Configuration;
import org.motechproject.nms.mobilekunji.repository.ConfigurationDataService;
import org.motechproject.nms.mobilekunji.service.ConfigurationService;
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

    public ConfigurationServiceImpl() {
    }


    /**
     * Create MobileKunji Service configuration
     *
     * @param configuration object to save
     */
    @Override
    public void createConfiguration(Configuration configuration) {
        configurationDataService.create(configuration);
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
