package org.motechproject.nms.mobileacademy.service.impl;

import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.Configuration;
import org.motechproject.nms.mobileacademy.repository.ConfigurationDataService;
import org.motechproject.nms.mobileacademy.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of ConfigurationService
 */
@Service("ConfigurationServiceMa")
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationDataService configurationDataService;

    @Override
    public void createConfiguration(Configuration configuration) {
        configurationDataService.create(configuration);
    }

    @Override
    public Configuration getConfiguration() {
        return configurationDataService
                .findByIndex(MobileAcademyConstants.CONFIG_DEFAULT_RECORD_INDEX);
    }

    @Override
    public Boolean isConfigurationPresent() {
        return getConfiguration() != null;
    }
}
