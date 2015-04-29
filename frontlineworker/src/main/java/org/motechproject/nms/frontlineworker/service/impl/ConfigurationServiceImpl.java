package org.motechproject.nms.frontlineworker.service.impl;

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


    @Override
    public void createConfiguration(Configuration configuration) {
        configurationDataService.create(configuration);
    }

    @Override
    public void updateConfiguration(Configuration configuration) {
        configurationDataService.update(configuration);
    }

    @Override
    public Configuration getConfiguration() {
        return configurationDataService.findRecordByIndex(ConfigurationConstants.CONFIGURATION_INDEX);
    }

    @Override
    public Boolean isConfigurationPresent() {
        return (getConfiguration() != null);
    }
}
