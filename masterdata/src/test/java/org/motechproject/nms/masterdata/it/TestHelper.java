package org.motechproject.nms.masterdata.it;

import org.motechproject.event.MotechEvent;
import org.motechproject.nms.masterdata.domain.*;

import java.util.*;

/**
 * This class is used as a test helper
 */
public class TestHelper {

    private static final String OPERATION = null;

    public static CsvState getStateCsvData() {

        CsvState csvStateData = new CsvState("UP", "123", null, null, "true", "true", "true", "true");
        csvStateData.setOwner("balvinder");
        csvStateData.setCreator("balvinder");
        csvStateData.setModifiedBy("balvinder");
        return csvStateData;
    }

    public static CsvState getInvalidStateCsvData() {

        CsvState csvStateData = new CsvState("Bihar", "abc123", null, null, "true", "true", "true", "true");
        csvStateData.setOwner("balvinder");
        csvStateData.setCreator("balvinder");
        csvStateData.setModifiedBy("balvinder");
        return csvStateData;
    }

    public static CsvState getUpdatedStateCsvData() {

        CsvState csvStateData = new CsvState("UK", "123", null, null, "true", "true", "true", "true");
        csvStateData.setOwner("balvinder");
        csvStateData.setCreator("balvinder");
        csvStateData.setModifiedBy("balvinder");
        return csvStateData;
    }

    public static State getStateData() {

        Set<District> districtSet = new HashSet<District>();

        State stateData = new State();
        stateData.setName("UP");
        stateData.setStateCode(123L);
        stateData.setMaCapping(100);
        stateData.setMkCapping(200);
        stateData.setDistrict(districtSet);
        return stateData;
    }

    public static CsvDistrict getDistrictCsvData() {

        CsvDistrict csvDistrictData = new CsvDistrict("Agra", "456", "123");
        return csvDistrictData;
    }

    public static CsvDistrict getInvalidDistrictCsvData() {

        CsvDistrict csvDistrictData = new CsvDistrict("Agra", "456", "456");
        return csvDistrictData;
    }

    public static CsvDistrict getUpdatedDistrictCsvData() {

        CsvDistrict csvDistrictData = new CsvDistrict("Aligarh", "456", "123");
        return csvDistrictData;
    }

    public static District getDistrictData() {

        Set<Taluka> talukaSet = new HashSet<Taluka>();

        District districtData = new District();
        districtData.setName("UP");
        districtData.setStateCode(123L);
        districtData.setDistrictCode(456L);
        districtData.setTaluka(talukaSet);

        return districtData;
    }

    public static CsvTaluka getTalukaCsvData() {

        CsvTaluka csvTalukaData = new CsvTaluka("Gabhana", "456", "123", "8");
        return csvTalukaData;
    }

    public static CsvTaluka getInvalidTalukaCsvData() {

        CsvTaluka csvTalukaData = new CsvTaluka("Gabhana", "456", "abc123", "8");
        return csvTalukaData;

    }

    public static CsvTaluka getUpdatedTalukaCsvData() {

        CsvTaluka csvTalukaData = new CsvTaluka("Ghabhana", "456", "123", "8");
        return csvTalukaData;
    }

    public static Taluka getTalukaData() {

        Set<HealthBlock> healthBlockSet = new HashSet<HealthBlock>();
        Set<Village> villageSet = new HashSet<Village>();

        Taluka talukaData = new Taluka();
        talukaData.setName("Gabhana");
        talukaData.setStateCode(123L);
        talukaData.setDistrictCode(456L);
        talukaData.setTalukaCode(8L);
        talukaData.setHealthBlock(healthBlockSet);
        talukaData.setVillage(villageSet);

        return talukaData;
    }

    public static CsvHealthBlock getHealthBlockCsvData() {

        CsvHealthBlock csvHealthBlockData = new CsvHealthBlock("Gangiri", "1002", "123", "456", "8");
        return csvHealthBlockData;
    }

    public static CsvHealthBlock getInvalidHealthBlockCsvData() {

        CsvHealthBlock csvHealthBlockData = new CsvHealthBlock("Gangiri", "1002", "abc123", "456", "8");
        return csvHealthBlockData;
    }

    public static CsvHealthBlock getUpdateHealthBlockCsvData() {

        CsvHealthBlock csvHealthBlockData = new CsvHealthBlock("Ganiri", "1002", "123", "456", "8");
        return csvHealthBlockData;
    }

    public static HealthBlock getHealthBlockData() {

        Set<HealthFacility> healthFacilitySet = new HashSet<HealthFacility>();

        HealthBlock healthBlockData = new HealthBlock();
        healthBlockData.setName("Gangiri");
        healthBlockData.setStateCode(123L);
        healthBlockData.setDistrictCode(456L);
        healthBlockData.setTalukaCode(8L);
        healthBlockData.setHealthBlockCode(1002L);
        healthBlockData.setHealthFacility(healthFacilitySet);

        return healthBlockData;
    }


    public static CsvVillage getVillageCsvData() {

        CsvVillage csvVillageData = new CsvVillage("Alampur", "122656", "123", "456", "8");
        return csvVillageData;
    }

    public static CsvVillage getInvalidVillageCsvData() {

        CsvVillage csvVillageData = new CsvVillage("Alampur", "122656", "abc123", "456", "8");
        return csvVillageData;
    }

    public static CsvVillage getUpdateVillageCsvData() {

        CsvVillage csvVillageData = new CsvVillage("Ahamadabad", "122656", "123", "456", "8");
        return csvVillageData;
    }

    public static HealthFacility getHealthFacilityData() {

        Set<HealthSubFacility> healthSubFacilitySet = new HashSet<HealthSubFacility>();

        HealthFacility healthFacilityData = new HealthFacility();
        healthFacilityData.setName("Gangiri");
        healthFacilityData.setStateCode(123L);
        healthFacilityData.setDistrictCode(456L);
        healthFacilityData.setTalukaCode(8L);
        healthFacilityData.setHealthBlockCode(1002L);
        healthFacilityData.setHealthFacilityCode(1111L);
        healthFacilityData.setHealthSubFacility(healthSubFacilitySet);

        return healthFacilityData;
    }

    public static CsvHealthFacility getHealthFacilityCsvData() {

        CsvHealthFacility csvHealthFacilityData = new CsvHealthFacility("HF1", "1111", "123", "456", "8", "1002", "9999");
        return csvHealthFacilityData;
    }

    public static CsvHealthFacility getInvalidHealthFacilityCsvData() {

        CsvHealthFacility csvHealthFacilityData = new CsvHealthFacility("HF1", "1111", "abc123", "456", "8", "1002", "9999");
        return csvHealthFacilityData;
    }

    public static CsvHealthFacility getUpdateHealthFacilityCsvData() {

        CsvHealthFacility csvHealthFacilityData = new CsvHealthFacility("HF2", "1111", "123", "456", "8", "1002", "9999");
        return csvHealthFacilityData;
    }

    public static CsvHealthSubFacility getHealthSubFacilityCsvData() {

        CsvHealthSubFacility csvHealthSubFacilityData = new CsvHealthSubFacility("HSF1", "1111", "9001", "123", "456", "8", "1002");
        return csvHealthSubFacilityData;
    }

    public static CsvHealthSubFacility getInvalidHealthSubFacilityCsvData() {

        CsvHealthSubFacility csvHealthSubFacilityData = new CsvHealthSubFacility("HSF1", "1111", "9001", "abc123", "456", "8", "1002");
        return csvHealthSubFacilityData;
    }

    public static CsvHealthSubFacility getUpdateHealthSubFacilityCsvData() {

        CsvHealthSubFacility csvHealthSubFacilityData = new CsvHealthSubFacility("HSF2", "1111", "9001", "123", "456", "8", "1002");
        return csvHealthSubFacilityData;
    }

    public static MotechEvent createMotechEvent(List<Long> ids, String event) {
        Map<String, Object> params = new HashMap<>();
        params.put("csv-import.created_ids", ids);
        params.put("csv-import.filename", "");
        return new MotechEvent(event, params);
    }

}
