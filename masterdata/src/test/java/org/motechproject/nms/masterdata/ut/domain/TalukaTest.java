package org.motechproject.nms.masterdata.ut.domain;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.it.TestHelper;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class is used to test(UT) the operations of Taluka
 */
public class TalukaTest {

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void testEquals() {

        Taluka taluka = TestHelper.getTalukaData();

        assertTrue(taluka.equals(taluka));
    }

    @Test
    public void testUnEquals() {

        Taluka taluka = TestHelper.getTalukaData();

        State state = TestHelper.getStateData();

        assertFalse(taluka.equals(state));
    }

    @Test
    public void testEqualsWithDifferentStateCode() {

        Taluka taluka = TestHelper.getTalukaData();

        assertFalse(getTalukaDataWithDifferentStateCode().equals(taluka));
    }

    @Test
    public void testEqualsWithDifferentDistrictCode() {

        Taluka taluka = TestHelper.getTalukaData();

        assertFalse(getTalukaDataWithDifferentDistrictCode().equals(taluka));
    }

    @Test
    public void testEqualsWithDifferentTalukaCode() {

        Taluka taluka = TestHelper.getTalukaData();

        assertFalse(getTalukaDataWithDifferentTalukaCode().equals(taluka));
    }

    @Test
    public void testToString() {
        Taluka taluka = TestHelper.getTalukaData();
        assertNotNull(taluka.toString());
    }

    @Test
    public void testHashCodeWithoutNull() {
        Taluka taluka = TestHelper.getTalukaData();
        assertNotNull(taluka.hashCode());
    }

    @Test
    public void testHashCodeWithNull() {
        Taluka taluka = TestHelper.getTalukaData();
        taluka.setStateCode(0L);
        taluka.setDistrictCode(0L);
        taluka.setTalukaCode(0L);
        assertTrue(0 == taluka.hashCode());
    }

    private Taluka getTalukaDataWithDifferentStateCode() {

        Taluka taluka = new Taluka();
        taluka.setStateCode(1L);
        taluka.setDistrictCode(456L);
        taluka.setTalukaCode(8L);

        return taluka;
    }

    private Taluka getTalukaDataWithDifferentDistrictCode() {
        Taluka taluka = new Taluka();
        taluka.setStateCode(123L);
        taluka.setDistrictCode(4L);
        taluka.setTalukaCode(8L);

        return taluka;
    }

    private Taluka getTalukaDataWithDifferentTalukaCode() {

        Taluka taluka = new Taluka();
        taluka.setStateCode(123L);
        taluka.setDistrictCode(456L);
        taluka.setTalukaCode(0L);

        return taluka;
    }
}
