package org.motechproject.nms.masterdata.ut.domain;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.it.TestHelper;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by abhishek on 11/3/15.
 */
public class HealthFacilityTest {

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void testEquals(){

        HealthFacility healthFacility = TestHelper.getHealthFacilityData();

        assertTrue(healthFacility.equals(healthFacility));
    }

    @Test
    public void testUnEquals(){

        HealthFacility healthFacility = TestHelper.getHealthFacilityData();

        State state = TestHelper.getStateData();

        assertFalse(healthFacility.equals(state));
    }

    @Test
    public void testEqualsWithDifferentStateCode(){

        HealthFacility healthFacility = TestHelper.getHealthFacilityData();

        assertFalse(getHealthFacilityDataWithDifferentStateCode().equals(healthFacility));
    }

    @Test
    public void testEqualsWithDifferentDistrictCode(){

        HealthFacility healthFacility = TestHelper.getHealthFacilityData();

        assertFalse(getHealthFacilityDataWithDifferentDistrictCode().equals(healthFacility));
    }

    @Test
    public void testEqualsWithDifferentTalukaCode(){

        HealthFacility healthFacility = TestHelper.getHealthFacilityData();

        assertFalse(getHealthFacilityDataWithDifferentTalukaCode().equals(healthFacility));
    }

    @Test
    public void testEqualsWithDifferentHealthBlockCode(){

        HealthFacility healthFacility = TestHelper.getHealthFacilityData();

        assertFalse(getHealthFacilityDataWithDifferentHealthBlockCode().equals(healthFacility));
    }

    @Test
    public void testEqualsWithDifferentHealthFacilityCode(){

        HealthFacility healthFacility = TestHelper.getHealthFacilityData();

        assertFalse(getHealthFacilityDataWithDifferentHealthFacilityCode().equals(healthFacility));
    }

    @Test
    public void testToString() {
        HealthFacility healthFacility = TestHelper.getHealthFacilityData();
        assertNotNull(healthFacility.toString());
    }

    @Test
    public void testHashCodeWithoutNull() {
        HealthFacility healthFacility = TestHelper.getHealthFacilityData();
        assertNotNull(healthFacility.hashCode());
    }

    @Test
    public void testHashCodeWithNull() {
        HealthFacility healthFacility = TestHelper.getHealthFacilityData();
        healthFacility.setStateCode(0L);
        healthFacility.setDistrictCode(0L);
        healthFacility.setTalukaCode(0);
        healthFacility.setHealthBlockCode(0L);
        healthFacility.setHealthFacilityCode(0L);
        assertTrue(0 == healthFacility.hashCode());
    }

    private HealthFacility getHealthFacilityDataWithDifferentStateCode() {

        HealthFacility healthFacility = new HealthFacility();
        healthFacility.setStateCode(1L);
        healthFacility.setDistrictCode(456L);
        healthFacility.setTalukaCode(8);
        healthFacility.setHealthBlockCode(1002L);
        healthFacility.setHealthFacilityCode(1111L);

        return healthFacility;
    }

    private HealthFacility getHealthFacilityDataWithDifferentDistrictCode() {

        HealthFacility healthFacility = new HealthFacility();
        healthFacility.setStateCode(123L);
        healthFacility.setDistrictCode(4L);
        healthFacility.setTalukaCode(8);
        healthFacility.setHealthBlockCode(1002L);
        healthFacility.setHealthFacilityCode(1111L);

        return healthFacility;
    }

    private HealthFacility getHealthFacilityDataWithDifferentTalukaCode() {

        HealthFacility healthFacility = new HealthFacility();
        healthFacility.setStateCode(123L);
        healthFacility.setDistrictCode(456L);
        healthFacility.setTalukaCode(9);
        healthFacility.setHealthBlockCode(1002L);
        healthFacility.setHealthFacilityCode(1111L);

        return healthFacility;
    }

    private HealthFacility getHealthFacilityDataWithDifferentHealthBlockCode() {

        HealthFacility healthFacility = new HealthFacility();
        healthFacility.setStateCode(123L);
        healthFacility.setDistrictCode(456L);
        healthFacility.setTalukaCode(8);
        healthFacility.setHealthBlockCode(1003L);
        healthFacility.setHealthFacilityCode(1112L);

        return healthFacility;
    }

    private HealthFacility getHealthFacilityDataWithDifferentHealthFacilityCode() {

        HealthFacility healthFacility = new HealthFacility();
        healthFacility.setStateCode(123L);
        healthFacility.setDistrictCode(456L);
        healthFacility.setTalukaCode(8);
        healthFacility.setHealthBlockCode(1002L);
        healthFacility.setHealthFacilityCode(1112L);

        return healthFacility;
    }

}
