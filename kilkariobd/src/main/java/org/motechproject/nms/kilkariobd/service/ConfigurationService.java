package org.motechproject.nms.kilkariobd.service;

import org.motechproject.nms.kilkariobd.domain.Configuration;

/**
 * The purpose of this class is to provide methods to create read the service level configuration properties.
 */
public interface ConfigurationService {

    /**
     * Create Kilkariobd Service configuration
     * @param configuration object to save
     */
    void createConfiguration(Configuration configuration);

    /**
     * get service configuration object
     * @return object corresponding to service configuration
     */
    Configuration getConfiguration();


    /**
     * get service configuration object
     * @param id
     * @return object corresponding to service configuration
     */
    Configuration getConfiguration(Long id);

    /**
     * Checks if the configuration is already present
     * @return true if present, else false
     */
    Boolean isConfigurationPresent();

}
