package org.motechproject.nms.masterdata.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.it.TestHelper;
import org.motechproject.nms.masterdata.repository.*;
import org.motechproject.nms.masterdata.service.LocationService;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by abhishek on 26/2/15.
 */


public class LocationServiceUnitTest {


    private LocationService locationService;

    @Mock
    private StateRecordsDataService stateRecordsDataService;

    @Mock
    private DistrictRecordsDataService districtRecordsDataService;

    @Mock
    private TalukaRecordsDataService talukaRecordsDataService;

    @Mock
    private HealthBlockRecordsDataService healthBlockRecordsDataService;

    @Mock
    private VillageRecordsDataService villageRecordsDataService;

    @Mock
    private HealthFacilityRecordsDataService healthFacilityRecordsDataService;

    @Mock
    private HealthSubFacilityRecordsDataService healthSubFacilityRecordsDataService;

    @Before
    public void setUp(){
        initMocks(this);
        locationService = new LocationServiceImpl(stateRecordsDataService,districtRecordsDataService,talukaRecordsDataService,healthBlockRecordsDataService,villageRecordsDataService,healthFacilityRecordsDataService,healthSubFacilityRecordsDataService);
    }

    @Test
    public void testValidationLocationSuccess() {

        State stateData = TestHelper.getStateData();
        stateData.setId(1L);

        District districtData = TestHelper.getDistrictData();
        districtData.setId(1L);

        when(stateRecordsDataService.findById(stateData.getId())).thenReturn(stateData);
        when(districtRecordsDataService.findById(districtData.getId())).thenReturn(districtData);
        when(districtRecordsDataService.findDistrictByParentCode(districtData.getStateCode(), districtData.getDistrictCode())).thenReturn(districtData);

        assertNotNull(locationService.validateLocation(1L, 1L));
        assertTrue(locationService.validateLocation(1L, 1L));
    }

    @Test
    public void testValidationLocationSuccessWithNullValue() {

        State stateData = TestHelper.getStateData();
        stateData.setId(1L);

        District districtData = TestHelper.getDistrictData();
        districtData.setId(1L);

        when(stateRecordsDataService.findById(stateData.getId())).thenReturn(null);
        when(districtRecordsDataService.findById(districtData.getId())).thenReturn(null);

        assertFalse(locationService.validateLocation(1L, 1L));
    }

    @Test
    public void testFindStateByCode(){

        State stateData = getStateData();

        when(stateRecordsDataService.findRecordByStateCode(123L)).thenReturn(stateData);

        assertNotNull(locationService.getStateByCode(stateData.getStateCode()));
        assertTrue(123L == locationService.getStateByCode(stateData.getStateCode()).getStateCode());
    }

    @Test
    public void testFindDistrictByCode(){

        State stateData = getStateData();
        District districtData = getDistrictData();

        when(stateRecordsDataService.findById(1L)).thenReturn(stateData);
        when(districtRecordsDataService.findDistrictByParentCode(456L,123L)).thenReturn(districtData);

        assertNotNull(locationService.getDistrictByCode(stateData.getId(), districtData.getDistrictCode()));
        assertTrue(456L == locationService.getDistrictByCode(stateData.getId(), districtData.getDistrictCode()).getDistrictCode());
    }

    @Test
    public void testFindTalukaByCode(){

        District districtData = getDistrictData();

        Taluka talukaData = getTalukaData();

        when(districtRecordsDataService.findById(1L)).thenReturn(districtData);

        when(talukaRecordsDataService.findTalukaByParentCode(districtData.getStateCode(),
                districtData.getDistrictCode(), "t123")).thenReturn(talukaData);

        assertNotNull(locationService.getTalukaByCode(1L,"t123"));
        assertTrue(123L == locationService.getTalukaByCode(1L, "t123").getStateCode());
        assertTrue(456L == locationService.getTalukaByCode(1L, "t123").getDistrictCode());
        assertTrue("t123".equals(locationService.getTalukaByCode(1L, "t123").getTalukaCode()));
    }

    @Test
    public void testGetHealthBlockByCode(){

        Taluka talukaData = getTalukaData();

        HealthBlock healthBlockData = getHealthBlockData();

        when(talukaRecordsDataService.findById(talukaData.getId())).thenReturn(talukaData);

        when(healthBlockRecordsDataService.findHealthBlockByParentCode(talukaData.getStateCode(),
                talukaData.getDistrictCode(), talukaData.getTalukaCode(), 789L)).thenReturn(healthBlockData);

        assertNotNull(locationService.getHealthBlockByCode(talukaData.getId(),789L));
        assertTrue(123L == locationService.getHealthBlockByCode(talukaData.getId(),789L).getStateCode());
        assertTrue(456L == locationService.getHealthBlockByCode(talukaData.getId(),789L).getDistrictCode());
        assertTrue("t123".equals(locationService.getHealthBlockByCode(talukaData.getId(),789L).getTalukaCode()));
        assertTrue(789L == locationService.getHealthBlockByCode(talukaData.getId(),789L).getHealthBlockCode());
    }

    @Test
    public void testVillageByCode() {
        Taluka talukaData = getTalukaData();

        Village villageData = getVillageData();

        when(talukaRecordsDataService.findById(talukaData.getId())).thenReturn(talukaData);

        when(villageRecordsDataService.findVillageByParentCode(talukaData.getStateCode(),
                talukaData.getDistrictCode(), talukaData.getTalukaCode(), 789L)).thenReturn(villageData);

        assertNotNull(locationService.getVillageByCode(talukaData.getId(),789L));
        assertTrue(123L == locationService.getVillageByCode(talukaData.getId(), 789L).getStateCode());
        assertTrue(456L == locationService.getVillageByCode(talukaData.getId(),789L).getDistrictCode());
        assertTrue("t123".equals(locationService.getVillageByCode(talukaData.getId(),789L).getTalukaCode()));
        assertTrue(789L == locationService.getVillageByCode(talukaData.getId(), 789L).getVillageCode());
    }

    @Test
    public void testVillageByCodeWithNullValue() {
        assertNull(locationService.getVillageByCode(null, null));
    }

    @Test
    public void testGetHealthFacilityByCode() {

        HealthBlock healthBlockData = new HealthBlock();
        healthBlockData.setId(1L);
        healthBlockData.setStateCode(123L);
        healthBlockData.setDistrictCode(456L);
        healthBlockData.setTalukaCode("t123");
        healthBlockData.setHealthBlockCode(789L);


        HealthFacility healthFacilityData = new HealthFacility();
        healthFacilityData.setStateCode(123L);
        healthFacilityData.setDistrictCode(456L);
        healthFacilityData.setTalukaCode("t123");
        healthFacilityData.setHealthBlockCode(789L);
        healthFacilityData.setHealthFacilityCode(321L);

        when(healthBlockRecordsDataService.findById(healthBlockData.getId())).thenReturn(healthBlockData);

        when(healthFacilityRecordsDataService.findHealthFacilityByParentCode(healthBlockData.getStateCode(),
                healthBlockData.getDistrictCode(), healthBlockData.getTalukaCode(), healthBlockData.getHealthBlockCode()
                , 321L)).thenReturn(healthFacilityData);

        assertNotNull(locationService.getHealthFacilityByCode(1L, 321l));
        assertTrue(123L == locationService.getHealthFacilityByCode(1L, 321l).getStateCode());
        assertTrue(456L == locationService.getHealthFacilityByCode(1L,321l).getDistrictCode() );
        assertTrue("t123".equals(locationService.getHealthFacilityByCode(1L,321l).getTalukaCode()));
        assertTrue(789L == locationService.getHealthFacilityByCode(1L,321l).getHealthBlockCode());
        assertTrue(321L == locationService.getHealthFacilityByCode(1L,321l).getHealthFacilityCode());
    }

    @Test
    public void testGetHealthFacilityByCodeWithNullValue() {
        assertNull(locationService.getHealthFacilityByCode(null, null));
    }

    @Test
    public void testGetHealthSubFacilityByCode(){

        HealthSubFacility healthSubFacilityData = new HealthSubFacility();
        healthSubFacilityData.setId(1L);
        healthSubFacilityData.setStateCode(123L);
        healthSubFacilityData.setDistrictCode(456L);
        healthSubFacilityData.setTalukaCode("t123");
        healthSubFacilityData.setHealthBlockCode(789L);
        healthSubFacilityData.setHealthFacilityCode(321L);


        HealthFacility healthFacilityData = new HealthFacility();
        healthFacilityData.setStateCode(123L);
        healthFacilityData.setDistrictCode(456L);
        healthFacilityData.setTalukaCode("t123");
        healthFacilityData.setHealthBlockCode(789L);
        healthFacilityData.setHealthFacilityCode(321L);

        when(healthFacilityRecordsDataService.findById(1L)).thenReturn(healthFacilityData);

        when(healthSubFacilityRecordsDataService.findHealthSubFacilityByParentCode(healthFacilityData.getStateCode(),
                healthFacilityData.getDistrictCode(), healthFacilityData.getTalukaCode(), healthFacilityData.getHealthBlockCode()
                , healthFacilityData.getHealthFacilityCode(),987L)).thenReturn(healthSubFacilityData);

        assertNotNull(locationService.getHealthSubFacilityByCode(1L, 987L));
        assertTrue(123L == locationService.getHealthSubFacilityByCode(1L, 987L).getStateCode());
        assertTrue(456L == locationService.getHealthSubFacilityByCode(1L, 987L).getDistrictCode() );
        assertTrue("t123".equals(locationService.getHealthSubFacilityByCode(1L, 987L).getTalukaCode()));
        assertTrue(789L == locationService.getHealthSubFacilityByCode(1L, 987L).getHealthBlockCode());
        assertTrue(321L == locationService.getHealthSubFacilityByCode(1L, 987L).getHealthFacilityCode());
    }

    @Test
    public void testGetHealthSubFacilityByCodeWithNullValue() {
        assertNull(locationService.getHealthSubFacilityByCode(null,null));
    }

    @Test
    public void testMaCapping() {

        State stateData = getStateData();

        when(stateRecordsDataService.findRecordByStateCode(stateData.getStateCode())).thenReturn(stateData);
        assertNotNull(locationService.getMaCappingByCode(stateData.getStateCode()));
        assertTrue(100 == locationService.getMaCappingByCode(stateData.getStateCode()).intValue());
    }

    @Test
    public void testMKCapping() {

        State stateData = getStateData();

        when(stateRecordsDataService.findRecordByStateCode(stateData.getStateCode())).thenReturn(stateData);
        assertNotNull(locationService.getMkCappingByCode(stateData.getStateCode()));
        assertTrue(200 == locationService.getMkCappingByCode(stateData.getStateCode()).intValue());
    }

    private State getStateData(){

        State stateData = new State();
        stateData.setId(1L);
        stateData.setStateCode(123L);
        stateData.setMaCapping(100);
        stateData.setMkCapping(200);
        return stateData;
    }

    private District getDistrictData(){
        District districtData = new District();
        districtData.setId(1L);
        districtData.setStateCode(123L);
        districtData.setDistrictCode(456L);
        return districtData;
    }

    private Taluka getTalukaData(){
        Taluka talukaData = new Taluka();
        talukaData.setId(1L);
        talukaData.setStateCode(123L);
        talukaData.setDistrictCode(456L);
        talukaData.setTalukaCode("t123");
        return talukaData;
    }

    private Village getVillageData(){
        Village villageData = new Village();
        villageData.setStateCode(123L);
        villageData.setDistrictCode(456L);
        villageData.setTalukaCode("t123");
        villageData.setVillageCode(789L);

        return villageData;
    }

    private HealthBlock getHealthBlockData(){
        HealthBlock healthBlockData = new HealthBlock();
        healthBlockData.setId(1L);
        healthBlockData.setStateCode(123L);
        healthBlockData.setDistrictCode(456L);
        healthBlockData.setTalukaCode("t123");
        healthBlockData.setHealthBlockCode(789L);
        return healthBlockData;
    }

}
