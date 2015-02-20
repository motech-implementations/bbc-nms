package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.Configuration;

/**
 * The purpose of this class is to provide methods to create read the service level configuration properties.
 */
public interface ConfigurationService {

        /**
         * Create Kilkari Service configuration
         * @param configuration object to save
         */
        void createConfiguration(Configuration configuration);

        /**
         * get service configuration object
         * @return object corresponding to service configuration
         */
        Configuration getConfiguration();

        /**
         * Checks if the configuration is already present
         * @return true if present, else false
         */
         Boolean isConfigurationPresent();


}


