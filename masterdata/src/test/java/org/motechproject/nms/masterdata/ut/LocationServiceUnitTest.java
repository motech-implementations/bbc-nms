package org.motechproject.nms.masterdata.ut;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaRecordsDataService;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.masterdata.service.impl.LocationServiceImpl;

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


    @Before
    public void setUp(){
        initMocks(this);
        locationService = new LocationServiceImpl();
    }

    @Test
    public void testFindStateByCode(){
        State stateData = new State();
        stateData.setStateCode(123L);
        when(stateRecordsDataService.findRecordByStateCode(123L)).thenReturn(stateData);
        assertNotNull(stateRecordsDataService.findRecordByStateCode(stateData.getStateCode()));
        assertTrue(stateRecordsDataService.findRecordByStateCode(stateData.getStateCode()).getStateCode() == stateData.getStateCode());
    }

    @Test
    public void testFindDistrictByCode(){
        District districtData = new District();
        districtData.setStateCode(123L);
        districtData.setDistrictCode(456L);
        when(districtRecordsDataService.findDistrictByParentCode(districtData.getDistrictCode(),districtData.getStateCode())).thenReturn(districtData);
        assertNotNull(districtRecordsDataService.findDistrictByParentCode(districtData.getDistrictCode(),districtData.getStateCode()));
    }

    @Test
    public void testFindTalukaByCode(){
        Taluka talukaData = new Taluka();
        talukaData.setStateCode(123L);
        talukaData.setDistrictCode(456L);
        talukaData.setTalukaCode("a123");
        when(talukaRecordsDataService.findTalukaByParentCode(talukaData.getStateCode(),
                talukaData.getDistrictCode(),talukaData.getTalukaCode())).thenReturn(talukaData);
        assertNotNull(talukaRecordsDataService.findTalukaByParentCode(talukaData.getStateCode(),
                talukaData.getDistrictCode(), talukaData.getTalukaCode()));
    }
}
