package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.repository.ConfigurationDataService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * This class provides the implementation of ConfigurationService
 */
@Service("configurationService")
public class ConfigurationServiceImpl implements ConfigurationService {
    
    public static final Long CONFIGURATION_INDEX = 1L;

    @Autowired
    private ConfigurationDataService configurationDataService;

    /**
     * Creates Configuration
     * @param configuration Configuration type object
     */
    @Override
    public void createConfiguration(Configuration configuration) {
        configurationDataService.create(configuration);
    }

    /**
     * gets configuration by configuration index
     * @return Configuration type objet
     */
    @Override
    public Configuration getConfiguration() {
        return configurationDataService.findConfigurationByIndex(CONFIGURATION_INDEX);
    }

    /**
     * check whether the configuration is present or not
     * @return Boolean type value
     */
    @Override
    public Boolean isConfigurationPresent() {
        return (getConfiguration() != null);
    }
}
