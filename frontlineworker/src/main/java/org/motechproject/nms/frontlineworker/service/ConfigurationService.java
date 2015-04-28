package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.Configuration;

/**
 * The purpose of this class is to provide methods to create read the service level configuration properties.
 */
public interface ConfigurationService {

    /**
     * Create FrontLineWorker Service configuration
     *
     * @param configuration object to save
     */
    void createConfiguration(Configuration configuration);

    /**
     * Create FrontLineWorker Service configuration
     *
     * @param configuration object to update
     */
    void updateConfiguration(Configuration configuration);

    /**
     * get service configuration object
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
