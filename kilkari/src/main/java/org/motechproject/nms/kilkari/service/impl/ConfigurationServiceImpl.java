package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.Configuration;
import static org.motechproject.nms.kilkari.constants.ConfigurationConstants.CONFIGURATION_INDEX;
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
    ConfigurationDataService configurationDataService;

    @Override
    public void createConfiguration(Configuration configuration) {
        configurationDataService.create(configuration);
    }

    @Override
    public Configuration getConfiguration() {
        return configurationDataService.findConfigurationByIndex(CONFIGURATION_INDEX);
    }

    @Override
    public Boolean isConfigurationPresent() {
        return (getConfiguration() != null);
    }
}
