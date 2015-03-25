package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.initializer.Initializer;
import org.motechproject.nms.kilkari.repository.ConfigurationDataService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * This class provides the implementation of ConfigurationService
 */
@Service("configurationService")
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationDataService configurationDataService;

    /**
     * Create Kilkari Service configuration
     * @param configuration object to save
     */
    @Override
    public void createConfiguration(Configuration configuration) {
        configurationDataService.create(configuration);
    }

    /**
     * get service configuration object
     * @return object corresponding to service configuration
     */
    @Override
    public Configuration getConfiguration() {
        return configurationDataService.findConfigurationByIndex(Initializer.CONFIGURATION_INDEX);
    }

    /**
     * Checks if the configuration is already present
     * @return true if present, else false
     */
    @Override
    public Boolean isConfigurationPresent() {
        return (configurationDataService.count() > Constants.COUNT_ZERO);
    }
}
