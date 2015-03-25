package org.motechproject.nms.mobilekunji.it.web;

import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.mobilekunji.domain.Configuration;

import java.util.HashSet;
import java.util.Set;

import static org.motechproject.nms.mobilekunji.constants.ConfigurationConstants.*;
import static org.motechproject.nms.mobilekunji.constants.ConfigurationConstants.DEFAULT_MAX_NATIONAL_CAPITAL_VALUE;

/**
 * Created by abhishek on 25/3/15.
 */
public class TestHelper {

    public static State getStateData() {

        Set<District> districtSet = new HashSet<District>();

        State stateData = new State();
        stateData.setName("UP");
        stateData.setStateCode(25L);
        stateData.setDistrict(districtSet);
        return stateData;
    }

    public static District getDistrictData() {

        District districtData = new District();
        districtData.setName("Meerut");
        districtData.setStateCode(25L);
        districtData.setDistrictCode(3L);

        return districtData;
    }

    public static Operator getOperatorData() {

        Operator operatorData = new Operator();

        operatorData.setName("Airtel");
        operatorData.setCode("AL");

        return operatorData;
    }

    public static Circle getCircleData() {

        Circle circleData = new Circle();

        circleData.setName("Delhi");
        circleData.setCode("DL");

        return circleData;
    }

    public static LanguageLocationCode getLanguageLocationCode() {

        LanguageLocationCode languageLocationCode = new LanguageLocationCode();

        languageLocationCode.setCircleCode("DL");
        languageLocationCode.setStateCode(25L);
        languageLocationCode.setDistrictCode(3L);

        languageLocationCode.setLanguageLocationCode(29);
        languageLocationCode.setLanguageMK("Hindi");
        languageLocationCode.setLanguageKK("Hindi");
        languageLocationCode.setLanguageMA("Hindi");

        return languageLocationCode;
    }

    public static Configuration getConfigurationData() {

        Configuration configuration = new Configuration();

        configuration.setIndex(CONFIGURATION_INDEX);
        configuration.setCappingType(DEFAULT_CAPPING_TYPE);
        configuration.setMaxWelcomeMessage(DEFAULT_MAX_WELCOME_MESSAGE);
        configuration.setMaxEndofusageMessage(DEFAULT_MAX_END_OF_USAGE_MESSAGE);
        configuration.setNationalCapValue(DEFAULT_MAX_NATIONAL_CAPITAL_VALUE);

        return configuration;
    }
}
