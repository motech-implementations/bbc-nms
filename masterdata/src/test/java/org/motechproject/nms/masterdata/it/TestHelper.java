package org.motechproject.nms.masterdata.it;

import org.motechproject.event.MotechEvent;
import org.motechproject.nms.masterdata.domain.*;

import java.util.*;

/**
 * Created by abhishek on 3/3/15.
 */
public class TestHelper {

    private static final String OPERATION=null;

    public static StateCsv getStateCsvData(){

        StateCsv stateCsvData = new StateCsv("Add","UP","123",null, null);
        return stateCsvData;
    }

    public static StateCsv getUpdatedStateCsvData(){

        StateCsv stateCsvData = new StateCsv("Add","UK","123",null, null);
        return stateCsvData;
    }

    public static StateCsv getDeleteStateCsvData(){

        StateCsv stateCsvData = new StateCsv("Del","UK","123",null, null);
        return stateCsvData;
    }

    public static State getStateData(){

        Set<District> districtSet = new HashSet<District>();

        State stateData = new State();
        stateData.setName("UP");
        stateData.setStateCode(123L);
        stateData.setMaCapping(100);
        stateData.setMkCapping(200);
        stateData.setDistrict(districtSet);
        return stateData;
    }

    public static DistrictCsv getDistrictCsvData(){

        DistrictCsv districtCsvData = new DistrictCsv("Add","Agra","456","123");
        return districtCsvData;
    }

    public static DistrictCsv getUpdatedDistrictCsvData(){

        DistrictCsv districtCsvData = new DistrictCsv("Add","Aligarh","456","123");
        return districtCsvData;
    }

    public static DistrictCsv getDeleteDistrictCsvData(){

        DistrictCsv districtCsvData = new DistrictCsv("Del","Aligarh","456","123");
        return districtCsvData;
    }

    public static District getDistrictData(){

        Set<Taluka> talukaSet = new HashSet<Taluka>();

        District districtData = new District();
        districtData.setName("UP");
        districtData.setStateCode(123L);
        districtData.setDistrictCode(456L);
        districtData.setTaluka(talukaSet);

        return districtData;
    }

    public static TalukaCsv getTalukaCsvData(){

        TalukaCsv talukaCsvData = new TalukaCsv("Add","Gabhana","456","123","8");
        return talukaCsvData;

    }

    public static TalukaCsv getUpdatedTalukaCsvData(){

        TalukaCsv talukaCsvData = new TalukaCsv("Add","Ghabhana","456","123","8");
        return talukaCsvData;
    }

    public static TalukaCsv getDeleteTalukaCsvData(){

        TalukaCsv talukaCsvData = new TalukaCsv("Del","Ghabhana","456","123","8");
        return talukaCsvData;
    }

    public static Taluka getTalukaData(){

        Set<HealthBlock> healthBlockSet = new HashSet<HealthBlock>();
        Set<Village> villageSet = new HashSet<Village>();

        Taluka talukaData = new Taluka();
        talukaData.setName("Gabhana");
        talukaData.setStateCode(123L);
        talukaData.setDistrictCode(456L);
        talukaData.setTalukaCode("8");
        talukaData.setHealthBlock(healthBlockSet);
        talukaData.setVillage(villageSet);

        return talukaData;
    }

    public static HealthBlockCsv getHealthBlockCsvData(){

        HealthBlockCsv healthBlockCsvData = new HealthBlockCsv("Add","Gangiri","1002","123","456","8");
        return healthBlockCsvData;
    }

    public static HealthBlockCsv getUpdateHealthBlockCsvData(){

        HealthBlockCsv healthBlockCsvData = new HealthBlockCsv("Add","Ganiri","1002","123","456","8");
        return healthBlockCsvData;
    }

    public static HealthBlockCsv getDeleteHealthBlockCsvData(){

        HealthBlockCsv healthBlockCsvData = new HealthBlockCsv("Del","Ganiri","1002","123","456","8");
        return healthBlockCsvData;
    }

    public static HealthBlock getHealthBlockData(){

        Set<HealthFacility> healthFacilitySet = new HashSet<HealthFacility>();

        HealthBlock healthBlockData = new HealthBlock();
        healthBlockData.setName("Gangiri");
        healthBlockData.setStateCode(123L);
        healthBlockData.setDistrictCode(456L);
        healthBlockData.setTalukaCode("8");
        healthBlockData.setHealthBlockCode(1002L);
        healthBlockData.setHealthFacility(healthFacilitySet);

        return healthBlockData;
    }


    public static VillageCsv getVillageCsvData() {

        VillageCsv villageCsvData = new VillageCsv("Add","Alampur","122656","123","456","8");
        return villageCsvData;
    }

    public static VillageCsv getUpdateVillageCsvData() {

        VillageCsv villageCsvData = new VillageCsv("Add","Ahamadabad","122656","123","456","8");
        return villageCsvData;
    }

    public static VillageCsv getDeleteVillageCsvData() {

        VillageCsv villageCsvData = new VillageCsv("Del","Ahamadabad","122656","123","456","8");
        return villageCsvData;
    }

    public static HealthFacility getHealthFacilityData(){

        Set<HealthSubFacility> healthSubFacilitySet = new HashSet<HealthSubFacility>();

        HealthFacility healthFacilityData = new HealthFacility();
        healthFacilityData.setName("Gangiri");
        healthFacilityData.setStateCode(123L);
        healthFacilityData.setDistrictCode(456L);
        healthFacilityData.setTalukaCode("8");
        healthFacilityData.setHealthBlockCode(1002L);
        healthFacilityData.setHealthFacilityCode(1111L);
        healthFacilityData.setHealthSubFacility(healthSubFacilitySet);

        return healthFacilityData;
    }

    public static HealthFacilityCsv getHealthFacilityCsvData(){

        HealthFacilityCsv healthFacilityCsvData = new HealthFacilityCsv("Add","HF1","1111","123","456","8","1002","9999");
        return healthFacilityCsvData;
    }

    public static HealthFacilityCsv getUpdateHealthFacilityCsvData(){

        HealthFacilityCsv healthFacilityCsvData = new HealthFacilityCsv("Add","HF2","1111","123","456","8","1002","9999");
        return healthFacilityCsvData;
    }

    public static HealthFacilityCsv getDeleteHealthFacilityCsvData(){

        HealthFacilityCsv healthFacilityCsvData = new HealthFacilityCsv("Del","HF2","1111","123","456","8","1002","9999");
        return healthFacilityCsvData;
    }

    public static HealthSubFacilityCsv getHealthSubFacilityCsvData(){

        HealthSubFacilityCsv healthSubFacilityCsvData = new HealthSubFacilityCsv("Add","HSF1","1111","9001","123","456","8","1002");
        return healthSubFacilityCsvData;
    }

    public static HealthSubFacilityCsv getUpdateHealthSubFacilityCsvData(){

        HealthSubFacilityCsv healthSubFacilityCsvData = new HealthSubFacilityCsv("Add","HSF2","1111","9001","123","456","8","1002");
        return healthSubFacilityCsvData;
    }

    public static HealthSubFacilityCsv getDeleteHealthSubFacilityCsvData(){

        HealthSubFacilityCsv healthSubFacilityCsvData = new HealthSubFacilityCsv("Del","HSF2","1111","9001","123","456","8","1002");
        return healthSubFacilityCsvData;
    }

    public static MotechEvent createMotechEvent(List<Long> ids,String event) {
        Map<String, Object> params = new HashMap<>();
        params.put("csv-import.created_ids", ids);
        params.put("csv-import.filename", "");
        return new MotechEvent(event, params);
    }

}
