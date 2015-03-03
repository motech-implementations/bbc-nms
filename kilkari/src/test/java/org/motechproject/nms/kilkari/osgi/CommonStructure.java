package org.motechproject.nms.kilkari.osgi;

import javax.inject.Inject;

import org.junit.Before;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.repository.HealthBlockRecordsDataService;
import org.motechproject.nms.masterdata.repository.HealthFacilityRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaRecordsDataService;
import org.motechproject.testing.osgi.BasePaxIT;

public class CommonStructure extends BasePaxIT {
    
    @Inject
    private StateRecordsDataService stateRecordsDataService;
    
    @Inject
    private DistrictRecordsDataService districtRecordsDataService;
    
    @Inject
    private TalukaRecordsDataService talukaRecordsDataService;
    
    @Inject
    private HealthBlockRecordsDataService healthBlockRecordsDataService;
    
    @Inject
    private HealthFacilityRecordsDataService healthFacilityRecordsDataService;

    private static boolean setUpIsDone = false;
    
    @Before
    public void setUp() {
        if (!setUpIsDone) {
            createState();
            createDistrict();
            createTaluka();
            createHealthBlock();
            createHealthFacility();
            createHealthSubFacility();
            createVillage();
        }
        // do the setup
        setUpIsDone = true;
    }

    private void createTaluka() {
        Taluka newRecord = new Taluka();
        newRecord.setName("taluka");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1L);
        newRecord.setTalukaCode("1");
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");
        District districtData = districtRecordsDataService.findDistrictByParentCode(newRecord.getDistrictCode(), newRecord.getStateCode());
        districtData.getTaluka().add(newRecord);
        districtRecordsDataService.update(districtData);
        System.out.println("Taluka data is successfully inserted.");
    }
    
    

    private void createDistrict() {
        District district = new District();
        district.setName("GGN");
        district.setStateCode(1L);
        district.setDistrictCode(1L);
        district.setCreator("Deepak");
        district.setOwner("Deepak");
        district.setModifiedBy("Deepak");
        State stateData = stateRecordsDataService.findRecordByStateCode(district.getStateCode());
        stateData.getDistrict().add(district);
        stateRecordsDataService.update(stateData);
        System.out.println("District data is successfully inserted.");
    }

    private void createState() {
        State state = new State();
        state.setName("HR");
        state.setStateCode(1L);
        state.setCreator("Deepak");
        state.setOwner("Deepak");
        state.setModifiedBy("Deepak");
        stateRecordsDataService.create(state);
        System.out.println("State data is successfully inserted.");
        
    }
    
    private void createHealthFacility(){
        HealthFacility newRecord = new HealthFacility();
        newRecord.setName("healthFacilityName");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1l);
        newRecord.setTalukaCode("1");
        newRecord.setHealthBlockCode(1l);
        newRecord.setHealthFacilityCode(1l);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");

        HealthBlock healthBlockData = healthBlockRecordsDataService.findHealthBlockByParentCode(
                newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode(), newRecord.getHealthBlockCode());
        healthBlockData.getHealthBlock().add(newRecord);
        healthBlockRecordsDataService.update(healthBlockData);
        System.out.println("HealthFacility data is successfully inserted.");
    }
    
    private void createHealthBlock(){
        HealthBlock newRecord = new HealthBlock();
        newRecord.setName("healthBlockName");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1L);
        newRecord.setTalukaCode("1");
        newRecord.setHealthBlockCode(1L);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");
        Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode());
        talukaRecord.getHealthBlock().add(newRecord);
        talukaRecordsDataService.update(talukaRecord);
        System.out.println("HealthBlock data is successfully inserted.");
    }
    
    private void createHealthSubFacility(){
        HealthSubFacility newRecord = new HealthSubFacility();
        newRecord.setName("healthSubFacilityName");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1l);
        newRecord.setTalukaCode("1");
        newRecord.setHealthBlockCode(1l);
        newRecord.setHealthFacilityCode(1l);
        newRecord.setHealthSubFacilityCode(1l);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");

        HealthFacility healthFacilityData = healthFacilityRecordsDataService.findHealthFacilityByParentCode(
                newRecord.getStateCode(), newRecord.getDistrictCode(),
                newRecord.getTalukaCode(), newRecord.getHealthBlockCode(),
                newRecord.getHealthFacilityCode());

        healthFacilityData.getHealthSubFacility().add(newRecord);
        healthFacilityRecordsDataService.update(healthFacilityData);
        System.out.println("HealthSubFacility Permanent data is successfully inserted.");
    }
    
    private void createVillage(){
        Village newRecord = new Village();
        newRecord.setName("villageName");
        newRecord.setStateCode(1L);
        newRecord.setDistrictCode(1l);
        newRecord.setTalukaCode("1");
        newRecord.setVillageCode(1l);
        newRecord.setCreator("Deepak");
        newRecord.setOwner("Deepak");
        newRecord.setModifiedBy("Deepak");

        Taluka talukaRecord = talukaRecordsDataService.findTalukaByParentCode(newRecord.getStateCode(), newRecord.getDistrictCode(), newRecord.getTalukaCode());
        talukaRecord.getVillage().add(newRecord);
        talukaRecordsDataService.update(talukaRecord);
        System.out.println("Village data is successfully inserted.");
    }
}
