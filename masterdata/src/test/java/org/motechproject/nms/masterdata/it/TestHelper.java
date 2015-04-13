package org.motechproject.nms.masterdata.it;

import org.motechproject.event.MotechEvent;
import org.motechproject.nms.masterdata.domain.*;

import java.util.*;

/**
 * This class is used as a test helper
 */
public class TestHelper {

    private static final String OPERATION = null;

    public static StateCsv getStateCsvData() {

        StateCsv stateCsvData = new StateCsv("UP", "123", null, null, "true", "true", "true", "true");
        stateCsvData.setOwner("balvinder");
        stateCsvData.setCreator("balvinder");
        stateCsvData.setModifiedBy("balvinder");
        return stateCsvData;
    }

    public static StateCsv getInvalidStateCsvData() {

        StateCsv stateCsvData = new StateCsv("Bihar", "abc123", null, null, "true", "true", "true", "true");
        stateCsvData.setOwner("balvinder");
        stateCsvData.setCreator("balvinder");
        stateCsvData.setModifiedBy("balvinder");
        return stateCsvData;
    }

    public static StateCsv getUpdatedStateCsvData() {

        StateCsv stateCsvData = new StateCsv("UK", "123", null, null, "true", "true", "true", "true");
        stateCsvData.setOwner("balvinder");
        stateCsvData.setCreator("balvinder");
        stateCsvData.setModifiedBy("balvinder");
        return stateCsvData;
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

    public static DistrictCsv getDistrictCsvData() {

        DistrictCsv districtCsvData = new DistrictCsv("Agra", "456", "123");
        return districtCsvData;
    }

    public static DistrictCsv getInvalidDistrictCsvData() {

        DistrictCsv districtCsvData = new DistrictCsv("Agra", "456", "456");
        return districtCsvData;
    }

    public static DistrictCsv getUpdatedDistrictCsvData() {

        DistrictCsv districtCsvData = new DistrictCsv("Aligarh", "456", "123");
        return districtCsvData;
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

    public static TalukaCsv getTalukaCsvData() {

        TalukaCsv talukaCsvData = new TalukaCsv("Gabhana", "456", "123", "8");
        return talukaCsvData;
    }

    public static TalukaCsv getInvalidTalukaCsvData() {

        TalukaCsv talukaCsvData = new TalukaCsv("Gabhana", "456", "abc123", "8");
        return talukaCsvData;

    }

    public static TalukaCsv getUpdatedTalukaCsvData() {

        TalukaCsv talukaCsvData = new TalukaCsv("Ghabhana", "456", "123", "8");
        return talukaCsvData;
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

    public static HealthBlockCsv getHealthBlockCsvData() {

        HealthBlockCsv healthBlockCsvData = new HealthBlockCsv("Gangiri", "1002", "123", "456", "8");
        return healthBlockCsvData;
    }

    public static HealthBlockCsv getInvalidHealthBlockCsvData() {

        HealthBlockCsv healthBlockCsvData = new HealthBlockCsv("Gangiri", "1002", "abc123", "456", "8");
        return healthBlockCsvData;
    }

    public static HealthBlockCsv getUpdateHealthBlockCsvData() {

        HealthBlockCsv healthBlockCsvData = new HealthBlockCsv("Ganiri", "1002", "123", "456", "8");
        return healthBlockCsvData;
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


    public static VillageCsv getVillageCsvData() {

        VillageCsv villageCsvData = new VillageCsv("Alampur", "122656", "123", "456", "8");
        return villageCsvData;
    }

    public static VillageCsv getInvalidVillageCsvData() {

        VillageCsv villageCsvData = new VillageCsv("Alampur", "122656", "abc123", "456", "8");
        return villageCsvData;
    }

    public static VillageCsv getUpdateVillageCsvData() {

        VillageCsv villageCsvData = new VillageCsv("Ahamadabad", "122656", "123", "456", "8");
        return villageCsvData;
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

    public static HealthFacilityCsv getHealthFacilityCsvData() {

        HealthFacilityCsv healthFacilityCsvData = new HealthFacilityCsv("HF1", "1111", "123", "456", "8", "1002", "9999");
        return healthFacilityCsvData;
    }

    public static HealthFacilityCsv getInvalidHealthFacilityCsvData() {

        HealthFacilityCsv healthFacilityCsvData = new HealthFacilityCsv("HF1", "1111", "abc123", "456", "8", "1002", "9999");
        return healthFacilityCsvData;
    }

    public static HealthFacilityCsv getUpdateHealthFacilityCsvData() {

        HealthFacilityCsv healthFacilityCsvData = new HealthFacilityCsv("HF2", "1111", "123", "456", "8", "1002", "9999");
        return healthFacilityCsvData;
    }

    public static HealthSubFacilityCsv getHealthSubFacilityCsvData() {

        HealthSubFacilityCsv healthSubFacilityCsvData = new HealthSubFacilityCsv("HSF1", "1111", "9001", "123", "456", "8", "1002");
        return healthSubFacilityCsvData;
    }

    public static HealthSubFacilityCsv getInvalidHealthSubFacilityCsvData() {

        HealthSubFacilityCsv healthSubFacilityCsvData = new HealthSubFacilityCsv("HSF1", "1111", "9001", "abc123", "456", "8", "1002");
        return healthSubFacilityCsvData;
    }

    public static HealthSubFacilityCsv getUpdateHealthSubFacilityCsvData() {

        HealthSubFacilityCsv healthSubFacilityCsvData = new HealthSubFacilityCsv("HSF2", "1111", "9001", "123", "456", "8", "1002");
        return healthSubFacilityCsvData;
    }

    public static MotechEvent createMotechEvent(List<Long> ids, String event) {
        Map<String, Object> params = new HashMap<>();
        params.put("csv-import.created_ids", ids);
        params.put("csv-import.filename", "");
        return new MotechEvent(event, params);
    }

}
