package org.motechproject.nms.frontlineworker.it.event.handler;

import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;

/**
 *
 */
public class TestHelper {

    private State state = new State();
    private District district = new District();
    private Circle circle = new Circle();
    private State stateTemp = new State();
    private District districtTemp = new District();

    public State createState() {

        state.setName("Delhi");
        state.setStateCode(12L);
        state.setCreator("Etasha");
        state.setMaCapping(10);
        state.setMkCapping(20);
        state.setIsMaDeployed(true);
        state.setIsMkDeployed(true);
        state.setIsWhiteListEnable(true);
        state.setOwner("Etasha");
        state.setModifiedBy("Etasha");
        return state;
    }

    public State createStateTemp() {

        stateTemp.setName("Rajasthan");
        stateTemp.setStateCode(13L);
        stateTemp.setCreator("Etasha");
        stateTemp.setMaCapping(10);
        stateTemp.setMkCapping(20);
        stateTemp.setIsMaDeployed(true);
        stateTemp.setIsMkDeployed(true);
        stateTemp.setIsWhiteListEnable(false);
        stateTemp.setOwner("Etasha");
        stateTemp.setModifiedBy("Etasha");
        return stateTemp;
    }

    public District createDistrict() {

        district.setName("East Delhi");
        district.setStateCode(12L);
        district.setDistrictCode(123L);
        district.setCreator("Etasha");
        district.setOwner("Etasha");
        district.setModifiedBy("Etasha");
        return district;
    }

    public District createDistrictTemp() {

        districtTemp.setName("Jaipur");
        districtTemp.setStateCode(13L);
        districtTemp.setDistrictCode(133L);
        districtTemp.setCreator("Etasha");
        districtTemp.setOwner("Etasha");
        districtTemp.setModifiedBy("Etasha");
        return districtTemp;
    }

    public Taluka createTaluka() {
        Taluka taluka = new Taluka();
        taluka.setName("taluka");
        taluka.setStateCode(12L);
        taluka.setDistrictCode(123L);
        taluka.setTalukaCode(1L);
        taluka.setCreator("Etasha");
        taluka.setOwner("Etasha");
        taluka.setModifiedBy("Etasha");
        return taluka;
    }

    public Village createVillage() {
        Village village = new Village();
        village.setName("villageName");
        village.setStateCode(12L);
        village.setDistrictCode(123L);
        village.setTalukaCode(1L);
        village.setVillageCode(1234L);
        village.setCreator("Etasha");
        village.setOwner("Etasha");
        village.setModifiedBy("Etasha");

        return village;
    }

    public HealthBlock createHealthBlock() {
        HealthBlock healthBlock = new HealthBlock();
        healthBlock.setName("healthBlockName");
        healthBlock.setStateCode(12L);
        healthBlock.setDistrictCode(123L);
        healthBlock.setTalukaCode(1L);
        healthBlock.setHealthBlockCode(1234L);
        healthBlock.setCreator("Etasha");
        healthBlock.setOwner("Etasha");
        healthBlock.setModifiedBy("Etasha");

        return healthBlock;
    }


    public HealthFacility createHealthFacility() {
        HealthFacility healthFacility = new HealthFacility();
        healthFacility.setName("healthFacilityName");
        healthFacility.setStateCode(12L);
        healthFacility.setDistrictCode(123L);
        healthFacility.setTalukaCode(1L);
        healthFacility.setHealthBlockCode(1234L);
        healthFacility.setHealthFacilityCode(12345L);
        healthFacility.setCreator("Etasha");
        healthFacility.setOwner("Etasha");
        healthFacility.setModifiedBy("Etasha");
        return healthFacility;


    }


    public HealthSubFacility createHealthSubFacility() {
        HealthSubFacility healthSubFacility = new HealthSubFacility();
        healthSubFacility.setName("healthSubFacilityName");
        healthSubFacility.setStateCode(12L);
        healthSubFacility.setDistrictCode(123L);
        healthSubFacility.setTalukaCode(1L);
        healthSubFacility.setHealthBlockCode(1234L);
        healthSubFacility.setHealthFacilityCode(12345L);
        healthSubFacility.setHealthSubFacilityCode(123456L);
        healthSubFacility.setCreator("Etasha");
        healthSubFacility.setOwner("Etasha");
        healthSubFacility.setModifiedBy("Etasha");

        return healthSubFacility;
    }

    public Circle createCircle() {
        circle.setName("circleName");
        circle.setCode("circleCode");
        circle.setCreator("Etasha");
        circle.setOwner("Etasha");
        circle.setModifiedBy("Etasha");
        circle.setDefaultLanguageLocationCode("LLC");
        return circle;

    }

    public LanguageLocationCode createLanguageLocationCode() {
        LanguageLocationCode languageLocationCode = new LanguageLocationCode();
        languageLocationCode.setState(state);
        languageLocationCode.setDistrict(district);
        languageLocationCode.setCircle(circle);
        languageLocationCode.setLanguageLocationCode("LLC");
        languageLocationCode.setLanguageMA("LanguageMA");
        languageLocationCode.setLanguageMK("LanguageMK");
        languageLocationCode.setLanguageKK("LanguageKK");
        languageLocationCode.setStateCode(1L);
        languageLocationCode.setDistrictCode(2L);
        languageLocationCode.setCircleCode("circleCode");
        languageLocationCode.setCreator("Etasha");
        languageLocationCode.setOwner("Etasha");
        languageLocationCode.setModifiedBy("Etasha");
        return languageLocationCode;
    }

    public Operator createOperator() {
        Operator operator = new Operator();
        operator.setName("operatorName");
        operator.setCode("123");
        operator.setCreator("Etasha");
        operator.setOwner("Etasha");
        operator.setModifiedBy("Etasha");
        return operator;
    }
}
