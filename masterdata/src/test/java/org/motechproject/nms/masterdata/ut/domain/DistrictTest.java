package org.motechproject.nms.masterdata.ut.domain;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.it.TestHelper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by abhishek on 11/3/15.
 */
public class DistrictTest {

    private District districtData;

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void testEqualsForStateCode(){

        District district = TestHelper.getDistrictData();

        assertFalse(getDistrictDataWithSameDistrictCode().equals(district));
    }

    @Test
    public void testEqualsForDistrictCode(){

        District district = TestHelper.getDistrictData();

        assertFalse(getDistrictData().equals(district));
    }

    @Test
    public void testEquals(){

        District district = TestHelper.getDistrictData();

        assertTrue(district.equals(district));
    }

    @Test
    public void testUnEquals(){

        District district = TestHelper.getDistrictData();

        State state = TestHelper.getStateData();

        assertFalse(district.equals(state));
    }

    private District getDistrictData() {

        District district = new District();
        district.setStateCode(1L);
        district.setDistrictCode(2L);

        return district;
    }

    private District getDistrictDataWithSameDistrictCode() {

        District district = new District();
        district.setStateCode(1L);
        district.setDistrictCode(456L);

        return district;
    }
}
