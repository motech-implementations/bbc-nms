package org.motechproject.nms.masterdata.ut.domain;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.it.TestHelper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by abhishek on 11/3/15.
 */
public class HealthSubFacilityTest {

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void testEquals(){

        HealthSubFacility healthSubFacility = getHealthSubFacilityData();

        assertTrue(healthSubFacility.equals(healthSubFacility));
    }

    @Test
    public void testUnEquals(){

        HealthSubFacility healthSubFacility = getHealthSubFacilityData();

        State state = TestHelper.getStateData();

        assertFalse(healthSubFacility.equals(state));
    }

    @Test
    public void testEqualsWithDifferentStateCode(){

        HealthSubFacility healthSubFacility = getHealthSubFacilityData();

        assertFalse(getHealthSubFacilityDataWithDifferentStateCode().equals(healthSubFacility));
    }

    @Test
    public void testEqualsWithDifferentDistrictCode(){

        HealthSubFacility healthSubFacility = getHealthSubFacilityData();

        assertFalse(getHealthSubFacilityDataWithDifferentDistrictCode().equals(healthSubFacility));
    }

    @Test
    public void testEqualsWithDifferentTalukaCode(){

        HealthSubFacility healthSubFacility = getHealthSubFacilityData();

        assertFalse(getHealthFacilityDataWithDifferentTalukaCode().equals(healthSubFacility));
    }

    @Test
    public void testEqualsWithDifferentHealthBlockCode(){

        HealthSubFacility healthSubFacility = getHealthSubFacilityData();

        assertFalse(getHealthSubFacilityDataWithDifferentHealthBlockCode().equals(healthSubFacility));
    }

    @Test
    public void testEqualsWithDifferentHealthFacilityCode(){

        HealthSubFacility healthSubFacility = getHealthSubFacilityData();

        assertFalse(getHealthSubFacilityDataWithDifferentHealthFacilityCode().equals(healthSubFacility));
    }

    @Test
    public void testEqualsWithDifferentHealthSubFacilityCode(){

        HealthSubFacility healthSubFacility = getHealthSubFacilityData();

        assertFalse(getHealthSubFacilityDataWithDifferentHealthSubFacilityCode().equals(healthSubFacility));
    }

    private HealthSubFacility getHealthSubFacilityDataWithDifferentStateCode() {

        HealthSubFacility healthSubFacilityData = new HealthSubFacility();

        healthSubFacilityData.setStateCode(1L);
        healthSubFacilityData.setDistrictCode(456L);
        healthSubFacilityData.setTalukaCode("8");
        healthSubFacilityData.setHealthBlockCode(1002L);
        healthSubFacilityData.setHealthFacilityCode(1111L);
        healthSubFacilityData.setHealthSubFacilityCode(9999L);

        return healthSubFacilityData;
    }

    private HealthSubFacility getHealthSubFacilityDataWithDifferentDistrictCode() {

        HealthSubFacility healthSubFacilityData = new HealthSubFacility();

        healthSubFacilityData.setStateCode(123L);
        healthSubFacilityData.setDistrictCode(2L);
        healthSubFacilityData.setTalukaCode("8");
        healthSubFacilityData.setHealthBlockCode(1002L);
        healthSubFacilityData.setHealthFacilityCode(1111L);
        healthSubFacilityData.setHealthSubFacilityCode(9999L);

        return healthSubFacilityData;
    }

    private HealthSubFacility getHealthFacilityDataWithDifferentTalukaCode() {

        HealthSubFacility healthSubFacilityData = new HealthSubFacility();
        healthSubFacilityData.setStateCode(123L);
        healthSubFacilityData.setDistrictCode(456L);
        healthSubFacilityData.setTalukaCode("9");
        healthSubFacilityData.setHealthBlockCode(1002L);
        healthSubFacilityData.setHealthFacilityCode(1111L);
        healthSubFacilityData.setHealthSubFacilityCode(9999L);

        return healthSubFacilityData;
    }

    private HealthSubFacility getHealthSubFacilityDataWithDifferentHealthBlockCode() {

        HealthSubFacility healthSubFacilityData = new HealthSubFacility();
        healthSubFacilityData.setName("Gangiri");
        healthSubFacilityData.setStateCode(123L);
        healthSubFacilityData.setDistrictCode(456L);
        healthSubFacilityData.setTalukaCode("8");
        healthSubFacilityData.setHealthBlockCode(1003L);
        healthSubFacilityData.setHealthFacilityCode(1111L);
        healthSubFacilityData.setHealthSubFacilityCode(9999L);

        return healthSubFacilityData;
    }

    private HealthSubFacility getHealthSubFacilityDataWithDifferentHealthFacilityCode() {

        HealthSubFacility healthSubFacilityData = new HealthSubFacility();
        healthSubFacilityData.setName("Gangiri");
        healthSubFacilityData.setStateCode(123L);
        healthSubFacilityData.setDistrictCode(456L);
        healthSubFacilityData.setTalukaCode("8");
        healthSubFacilityData.setHealthBlockCode(1002L);
        healthSubFacilityData.setHealthFacilityCode(1112L);
        healthSubFacilityData.setHealthSubFacilityCode(9999L);

        return healthSubFacilityData;
    }

    private HealthSubFacility getHealthSubFacilityDataWithDifferentHealthSubFacilityCode() {

        HealthSubFacility healthSubFacilityData = new HealthSubFacility();
        healthSubFacilityData.setName("Gangiri");
        healthSubFacilityData.setStateCode(123L);
        healthSubFacilityData.setDistrictCode(456L);
        healthSubFacilityData.setTalukaCode("8");
        healthSubFacilityData.setHealthBlockCode(1002L);
        healthSubFacilityData.setHealthFacilityCode(1111L);
        healthSubFacilityData.setHealthSubFacilityCode(9L);

        return healthSubFacilityData;
    }

    public static HealthSubFacility getHealthSubFacilityData(){

        HealthSubFacility healthSubFacilityData = new HealthSubFacility();
        healthSubFacilityData.setName("Gangiri");
        healthSubFacilityData.setStateCode(123L);
        healthSubFacilityData.setDistrictCode(456L);
        healthSubFacilityData.setTalukaCode("8");
        healthSubFacilityData.setHealthBlockCode(1002L);
        healthSubFacilityData.setHealthFacilityCode(1111L);
        healthSubFacilityData.setHealthSubFacilityCode(9999L);

        return healthSubFacilityData;
    }
}
