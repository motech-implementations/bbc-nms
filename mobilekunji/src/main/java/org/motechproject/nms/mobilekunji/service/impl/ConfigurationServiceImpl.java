package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.Configuration;
import org.motechproject.nms.mobilekunji.repository.ConfigurationDataService;
import org.motechproject.nms.mobilekunji.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.motechproject.nms.mobilekunji.constants.ConfigurationConstants.CONFIGURATION_INDEX;

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


    @Override
    public void createConfiguration(Configuration configuration) {
        configurationDataService.create(configuration);
    }

    @Override
    public Configuration getConfiguration() {
        return configurationDataService.findRecordByIndex(CONFIGURATION_INDEX);
    }

    @Override
    public Boolean isConfigurationPresent() {
        return (getConfiguration() != null);
    }


}
