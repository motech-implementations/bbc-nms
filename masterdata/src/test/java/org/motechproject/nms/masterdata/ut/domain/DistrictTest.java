package org.motechproject.nms.masterdata.ut.domain;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.it.TestHelper;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class is used to test(UT) the operations of District
 */
public class DistrictTest {

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void testEquals() {

        District district = TestHelper.getDistrictData();

        assertTrue(district.equals(district));
    }

    @Test
    public void testUnEquals() {

        District district = TestHelper.getDistrictData();

        State state = TestHelper.getStateData();

        assertFalse(district.equals(state));
    }

    @Test
    public void testEqualsWithSameDistrictCode()
    {
        District district = TestHelper.getDistrictData();

        assertTrue(TestHelper.getDistrictData().equals(district));
    }

    @Test
    public void testEqualsWithDifferentStateCode() {

        District district = TestHelper.getDistrictData();

        assertFalse(getDistrictDataWithDifferentStateCode().equals(district));
    }

    @Test
    public void testEqualsWithSameStateCode()
    {
        District district = getDistrictDataWithDifferentStateCode();

        assertTrue(getDistrictDataWithDifferentStateCode().equals(district));
    }


    @Test
    public void testToString() {
        District district = TestHelper.getDistrictData();
        assertNotNull(district.toString());
    }

    @Test
    public void testHashCodeWithoutNull() {
        District district = TestHelper.getDistrictData();
        assertNotNull(district.hashCode());
    }

    @Test
    public void testHashCodeWithNull() {
        District district = TestHelper.getDistrictData();
        district.setStateCode(null);
        district.setDistrictCode(null);
        assertTrue(0 == district.hashCode());
    }

    private District getDistrictDataWithDifferentStateCode() {

        District district = new District();
        district.setStateCode(1L);
        district.setDistrictCode(456L);

        return district;
    }

    private District getDistrictDataWithDifferentDistrictCode() {

        District district = new District();
        district.setStateCode(123L);
        district.setDistrictCode(4L);

        return district;
    }
}
