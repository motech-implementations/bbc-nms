package org.motechproject.nms.mobileacademy.service;

import org.motechproject.nms.mobileacademy.domain.Configuration;

/**
 * The purpose of this class is to provide methods to perform operations on
 * Configuration table.
 */
public interface ConfigurationService {

    /**
     * Create new service configuration
     * 
     * @param configuration object to save
     */
    void createConfiguration(Configuration configuration);

    /**
     * Get service configuration object using
     * SERVICE_CONFIG_DEFAULT_RECORD_INDEX i.e 1L
     * 
     * @return object corresponding to service configuration
     */
    Configuration getConfiguration();

    /**
     * Checks if the configuration is already present
     * 
     * @return true if present, else false
     */
    Boolean isConfigurationPresent();

}
