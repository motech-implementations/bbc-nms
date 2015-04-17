package org.motechproject.nms.mobilekunji.it.web;

import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.mobilekunji.domain.CardDetail;
import org.motechproject.nms.mobilekunji.domain.Configuration;
import org.motechproject.nms.mobilekunji.dto.LanguageLocationCodeApiRequest;
import org.motechproject.nms.mobilekunji.dto.SaveCallDetailApiRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.motechproject.nms.mobilekunji.constants.ConfigurationConstants.*;

/**
 * This is a helper class in which data is set for test class.
 */
public class TestHelper {

    public static State getStateData() {

        Set<District> districtSet = new HashSet<District>();

        State stateData = new State();
        stateData.setName("UP");
        stateData.setStateCode(25L);
        stateData.setIsKkDeployed(true);
        stateData.setIsMaDeployed(true);
        stateData.setIsMkDeployed(true);
        stateData.setIsWhiteListEnable(true);
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

    public static Circle getInvalidCircleData() {

        Circle circleData = new Circle();

        circleData.setName("Meerut");
        circleData.setCode("99");

        return circleData;
    }

    public static LanguageLocationCode getLanguageLocationCode() {

        LanguageLocationCode languageLocationCode = new LanguageLocationCode();

        languageLocationCode.setCircleCode("DL");
        languageLocationCode.setStateCode(25L);
        languageLocationCode.setDistrictCode(3L);

        languageLocationCode.setLanguageLocationCode("29");
        languageLocationCode.setLanguageMK("Hindi");
        languageLocationCode.setLanguageKK("Hindi");
        languageLocationCode.setLanguageMA("Hindi");

        return languageLocationCode;
    }

    public static Configuration getConfigurationData() {

        Configuration configuration = new Configuration();

        configuration.setIndex(CONFIGURATION_INDEX);
        configuration.setCappingType(DEFAULT_CAPPING_TYPE);
        configuration.setMaxEndofusageMessage(DEFAULT_MAX_END_OF_USAGE_MESSAGE);
        configuration.setNationalCapValue(DEFAULT_MAX_NATIONAL_CAPITAL_VALUE);

        return configuration;
    }

    public static LanguageLocationCodeApiRequest getLanguageLocationCodeRequest() {

        LanguageLocationCodeApiRequest request = new LanguageLocationCodeApiRequest();
        request.setCallId("111111111111111");
        request.setCallingNumber("9810179788");
        request.setLanguageLocationCode("29");

        return request;
    }

    public static SaveCallDetailApiRequest getSaveCallDetailApiRequest() {

        SaveCallDetailApiRequest request = new SaveCallDetailApiRequest();

        request.setContent(getCardDetailList());
        request.setCallId("234000011111111");
        request.setCallingNumber("9810179788");
        request.setOperator("AL");
        request.setCircle("DL");
        request.setCallDurationInPulses(60);
        request.setEndOfUsagePromptCounter(0);
        request.setWelcomeMessagePromptFlag(false);
        request.setCallDisconnectReason("1");
        request.setCallStartTime(1427372125L);
        request.setCallEndTime(1427372128L);

        return request;
    }

    public static List<CardDetail> getCardDetailList() {

        List<CardDetail> cardDetailList = new ArrayList<>();

        CardDetail cardDetail = new CardDetail();
        cardDetail.setMkCardNumber("01");
        cardDetail.setAudioFileName("Yellowfever.wav");
        cardDetail.setContentName("YellowFever");
        cardDetail.setEndTime(1222222221);
        cardDetail.setStartTime(1200000000);

        cardDetailList.add(cardDetail);

        return cardDetailList;
    }

    public static HttpRequest getHttpRequest() {
        HttpRequest request = new HttpRequest();

        return request;
    }

}
