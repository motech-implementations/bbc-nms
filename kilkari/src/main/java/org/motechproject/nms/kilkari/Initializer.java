package org.motechproject.nms.kilkari;

import org.motechproject.nms.kilkari.constants.ConfigurationConstants;
import org.motechproject.nms.kilkari.domain.Configuration;
import org.motechproject.nms.kilkari.repository.ConfigurationDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * The purpose of this class is to perform initialization for Kilkari Service.
 * It creates and initializes the configuration parameters with default values, if not created already
 *
 */
@Component
public class Initializer {

    private static final Logger LOG = LoggerFactory.getLogger(Initializer.class);


    @Autowired
    ConfigurationDataService configurationDataService;

    @PostConstruct
    public void initializeConfiguration() {

        Configuration configuration = new Configuration();

        configuration.setNmsKk48WeeksPackMsgsPerWeek(ConfigurationConstants.DEFAULT_48_WEEKS_PACK_MSGS_PER_WEEK);
        configuration.setNmsKk48WeeksPackMsgsPerWeek(ConfigurationConstants.DEFAULT_72_WEEKS_PACK_MSGS_PER_WEEK);
        configuration.setNmsKkMaxAllowedActiveBeneficiaryCount(ConfigurationConstants.DEFAULT_ALLOWED_BENEFICIARY_COUNT);
    }


}
