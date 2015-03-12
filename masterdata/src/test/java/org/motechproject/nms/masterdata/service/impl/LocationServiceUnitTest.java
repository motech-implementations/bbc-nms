package org.motechproject.nms.masterdata.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.it.TestHelper;
import org.motechproject.nms.masterdata.repository.HealthSubFacilityRecordsDataService;
import org.motechproject.nms.masterdata.service.*;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LocationServiceUnitTest {


    private LocationService locationService;

    @Mock
    private StateService stateService;

    @Mock
    private DistrictService districtService;

    @Mock
    private TalukaService talukaService;

    @Mock
    private HealthBlockService healthBlockService;

    @Mock
    private VillageService villageService;

    @Mock
    private HealthFacilityService healthFacilityService;

    @Mock
    private HealthSubFacilityRecordsDataService healthSubFacilityRecordsDataService;

    @Before
    public void setUp() {
        initMocks(this);
        locationService = new LocationServiceImpl(stateService, districtService, talukaService, healthBlockService, villageService, healthFacilityService, healthSubFacilityRecordsDataService);
    }

    @Test
    public void testValidationLocationSuccess() {

        State stateData = TestHelper.getStateData();
        stateData.setId(1L);

        District districtData = TestHelper.getDistrictData();
        districtData.setId(1L);

        when(stateService.findById(stateData.getId())).thenReturn(stateData);
        when(districtService.findById(districtData.getId())).thenReturn(districtData);
        when(districtService.findDistrictByParentCode(districtData.getStateCode(), districtData.getDistrictCode())).thenReturn(districtData);

        assertNotNull(locationService.validateLocation(1L, 1L));
        assertTrue(locationService.validateLocation(1L, 1L));
    }

    @Test
    public void testValidationLocationSuccessWithStateValue() {

        State stateData = TestHelper.getStateData();
        stateData.setId(1L);

        District districtData = TestHelper.getDistrictData();
        districtData.setId(1L);

        when(stateService.findById(1L)).thenReturn(stateData);
        when(districtService.findById(1L)).thenReturn(null);

        assertFalse(locationService.validateLocation(1L, 1L));
    }

    @Test
    public void testValidationLocationSuccessWithDistrictValue() {

        State stateData = TestHelper.getStateData();
        stateData.setId(1L);

        District districtData = TestHelper.getDistrictData();
        districtData.setId(1L);

        when(stateService.findById(1L)).thenReturn(null);
        when(districtService.findById(1L)).thenReturn(districtData);

        assertFalse(locationService.validateLocation(1L, 1L));
    }

    @Test
    public void testValidationLocationSuccessWithNullValue() {

        State stateData = TestHelper.getStateData();
        stateData.setId(1L);

        District districtData = TestHelper.getDistrictData();
        districtData.setId(1L);

        when(stateService.findById(stateData.getId())).thenReturn(null);
        when(districtService.findById(districtData.getId())).thenReturn(null);

        assertFalse(locationService.validateLocation(1L, 1L));
    }

    @Test
    public void testFindStateByCode() {

        State stateData = getStateData();

        when(stateService.findRecordByStateCode(123L)).thenReturn(stateData);

        assertNotNull(locationService.getStateByCode(stateData.getStateCode()));
        assertTrue(123L == locationService.getStateByCode(stateData.getStateCode()).getStateCode());
    }

    @Test
    public void testFindDistrictByCode() {

        State stateData = getStateData();
        District districtData = getDistrictData();

        when(stateService.findById(1L)).thenReturn(stateData);
        when(districtService.findDistrictByParentCode(456L, 123L)).thenReturn(districtData);

        assertNotNull(locationService.getDistrictByCode(stateData.getId(), districtData.getDistrictCode()));
        assertTrue(456L == locationService.getDistrictByCode(stateData.getId(), districtData.getDistrictCode()).getDistrictCode());
    }

    @Test
    public void testFindDistrictByCodeWIthNullValue() {
        assertNull(locationService.getDistrictByCode(null, null));
    }

    @Test
    public void testGetTalukaByCodeSucess() {

        District districtData = getDistrictData();

        Taluka talukaData = getTalukaData();

        when(districtService.findById(1L)).thenReturn(districtData);

        when(talukaService.findTalukaByParentCode(districtData.getStateCode(),
                districtData.getDistrictCode(), 8L)).thenReturn(talukaData);

        assertNotNull(locationService.getTalukaByCode(1L, 8L));
        assertTrue(123L == locationService.getTalukaByCode(1L, 8L).getStateCode());
        assertTrue(456L == locationService.getTalukaByCode(1L, 8L).getDistrictCode());
        assertTrue(8L == locationService.getTalukaByCode(1L, 8L).getTalukaCode());
    }

    @Test
    public void testGetTalukaByCodeWithNull() {
        assertNull(locationService.getTalukaByCode(null, null));
    }

    @Test
    public void testGetHealthBlockByCode() {

        Taluka talukaData = getTalukaData();

        HealthBlock healthBlockData = getHealthBlockData();

        when(talukaService.findById(1L)).thenReturn(talukaData);

        when(healthBlockService.findHealthBlockByParentCode(123L,
                456L, 8L, 789L)).thenReturn(healthBlockData);

        assertNotNull(locationService.getHealthBlockByCode(1L, 789L));
        assertTrue(123L == locationService.getHealthBlockByCode(1L, 789L).getStateCode());
        assertTrue(456L == locationService.getHealthBlockByCode(1L, 789L).getDistrictCode());
        assertTrue(8L == locationService.getHealthBlockByCode(1L, 789L).getTalukaCode());
        assertTrue(789L == locationService.getHealthBlockByCode(1L, 789L).getHealthBlockCode());
    }

    @Test
    public void testGetHealthByCodeWithTalukaId() {
        assertNull(locationService.getHealthBlockByCode(1L, null));
    }

    @Test
    public void testGetHealthByCodeWithHealthBlockCode() {
        assertNull(locationService.getHealthBlockByCode(null, 1L));
    }

    @Test
    public void testGetHealthBlockByCodeWithNull() {
        assertNull(locationService.getHealthBlockByCode(null, null));
    }

    @Test
    public void testVillageByCode() {
        Taluka talukaData = getTalukaData();

        Village villageData = getVillageData();

        when(talukaService.findById(talukaData.getId())).thenReturn(talukaData);

        when(villageService.findVillageByParentCode(talukaData.getStateCode(),
                talukaData.getDistrictCode(), talukaData.getTalukaCode(), 789L)).thenReturn(villageData);

        assertNotNull(locationService.getVillageByCode(talukaData.getId(), 789L));
        assertTrue(123L == locationService.getVillageByCode(talukaData.getId(), 789L).getStateCode());
        assertTrue(456L == locationService.getVillageByCode(talukaData.getId(), 789L).getDistrictCode());
        assertTrue(8L == locationService.getVillageByCode(talukaData.getId(), 789L).getTalukaCode());
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
        healthBlockData.setTalukaCode(8L);
        healthBlockData.setHealthBlockCode(789L);


        HealthFacility healthFacilityData = new HealthFacility();
        healthFacilityData.setStateCode(123L);
        healthFacilityData.setDistrictCode(456L);
        healthFacilityData.setTalukaCode(8L);
        healthFacilityData.setHealthBlockCode(789L);
        healthFacilityData.setHealthFacilityCode(321L);

        when(healthBlockService.findById(healthBlockData.getId())).thenReturn(healthBlockData);

        when(healthFacilityService.findHealthFacilityByParentCode(healthBlockData.getStateCode(),
                healthBlockData.getDistrictCode(), healthBlockData.getTalukaCode(), healthBlockData.getHealthBlockCode()
                , 321L)).thenReturn(healthFacilityData);

        assertNotNull(locationService.getHealthFacilityByCode(1L, 321l));
        assertTrue(123L == locationService.getHealthFacilityByCode(1L, 321l).getStateCode());
        assertTrue(456L == locationService.getHealthFacilityByCode(1L, 321l).getDistrictCode());
        assertTrue(8L == locationService.getHealthFacilityByCode(1L, 321l).getTalukaCode());
        assertTrue(789L == locationService.getHealthFacilityByCode(1L, 321l).getHealthBlockCode());
        assertTrue(321L == locationService.getHealthFacilityByCode(1L, 321l).getHealthFacilityCode());
    }

    @Test
    public void testGetHealthFacilityByCodeWithNullValue() {
        assertNull(locationService.getHealthFacilityByCode(null, null));
    }

    @Test
    public void testGetHealthSubFacilityByCode() {

        HealthSubFacility healthSubFacilityData = new HealthSubFacility();
        healthSubFacilityData.setId(1L);
        healthSubFacilityData.setStateCode(123L);
        healthSubFacilityData.setDistrictCode(456L);
        healthSubFacilityData.setTalukaCode(8L);
        healthSubFacilityData.setHealthBlockCode(789L);
        healthSubFacilityData.setHealthFacilityCode(321L);


        HealthFacility healthFacilityData = new HealthFacility();
        healthFacilityData.setStateCode(123L);
        healthFacilityData.setDistrictCode(456L);
        healthFacilityData.setTalukaCode(8L);
        healthFacilityData.setHealthBlockCode(789L);
        healthFacilityData.setHealthFacilityCode(321L);

        when(healthFacilityService.findById(1L)).thenReturn(healthFacilityData);

        when(healthSubFacilityRecordsDataService.findHealthSubFacilityByParentCode(healthFacilityData.getStateCode(),
                healthFacilityData.getDistrictCode(), healthFacilityData.getTalukaCode(), healthFacilityData.getHealthBlockCode()
                , healthFacilityData.getHealthFacilityCode(), 987L)).thenReturn(healthSubFacilityData);

        assertNotNull(locationService.getHealthSubFacilityByCode(1L, 987L));
        assertTrue(123L == locationService.getHealthSubFacilityByCode(1L, 987L).getStateCode());
        assertTrue(456L == locationService.getHealthSubFacilityByCode(1L, 987L).getDistrictCode());
        assertTrue(8L == locationService.getHealthSubFacilityByCode(1L, 987L).getTalukaCode());
        assertTrue(789L == locationService.getHealthSubFacilityByCode(1L, 987L).getHealthBlockCode());
        assertTrue(321L == locationService.getHealthSubFacilityByCode(1L, 987L).getHealthFacilityCode());
    }

    @Test
    public void testGetHealthSubFacilityByCodeWithNullValue() {
        assertNull(locationService.getHealthSubFacilityByCode(null, null));
    }

    @Test
    public void testMaCapping() {

        State stateData = getStateData();

        when(stateService.findRecordByStateCode(stateData.getStateCode())).thenReturn(stateData);
        assertNotNull(locationService.getMaCappingByCode(stateData.getStateCode()));
        assertTrue(100 == locationService.getMaCappingByCode(stateData.getStateCode()).intValue());
    }

    @Test
    public void testMKCapping() {

        State stateData = getStateData();

        when(stateService.findRecordByStateCode(stateData.getStateCode())).thenReturn(stateData);
        assertNotNull(locationService.getMkCappingByCode(stateData.getStateCode()));
        assertTrue(200 == locationService.getMkCappingByCode(stateData.getStateCode()).intValue());
    }

    private State getStateData() {

        State stateData = new State();
        stateData.setId(1L);
        stateData.setStateCode(123L);
        stateData.setMaCapping(100);
        stateData.setMkCapping(200);
        return stateData;
    }

    private District getDistrictData() {
        District districtData = new District();
        districtData.setId(1L);
        districtData.setStateCode(123L);
        districtData.setDistrictCode(456L);
        return districtData;
    }

    private Taluka getTalukaData() {
        Taluka talukaData = new Taluka();
        talukaData.setId(1L);
        talukaData.setStateCode(123L);
        talukaData.setDistrictCode(456L);
        talukaData.setTalukaCode(8L);
        return talukaData;
    }

    private Village getVillageData() {
        Village villageData = new Village();
        villageData.setStateCode(123L);
        villageData.setDistrictCode(456L);
        villageData.setTalukaCode(8L);
        villageData.setVillageCode(789L);

        return villageData;
    }

    private HealthBlock getHealthBlockData() {
        HealthBlock healthBlockData = new HealthBlock();
        healthBlockData.setId(1L);
        healthBlockData.setStateCode(123L);
        healthBlockData.setDistrictCode(456L);
        healthBlockData.setTalukaCode(8L);
        healthBlockData.setHealthBlockCode(789L);
        return healthBlockData;
    }

}
