package org.motechproject.nms.mobileacademy.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobileacademy.domain.Configuration;

/**
 * Interface for repository that persists simple records and allows CRUD on
 * Configuration table. MotechDataService base class will provide the
 * implementation of this class as well as methods for adding, deleting, saving
 * and finding all instances. In this class we define and custom lookups we may
 * need.
 */
public interface ConfigurationDataService extends
        MotechDataService<Configuration> {

    /**
     * find By Index field
     * 
     * @param index
     * @return ServiceConfigParam
     */
    @Lookup
    Configuration findByIndex(@LookupField(name = "index") Long index);

}