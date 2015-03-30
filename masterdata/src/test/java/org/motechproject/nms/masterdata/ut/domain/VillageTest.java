package org.motechproject.nms.masterdata.ut.domain;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.it.TestHelper;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class is used to test(UT) the operations of Village
 */
public class VillageTest {

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void testEquals() {

        Village village = getVillageData();

        assertTrue(village.equals(village));
    }

    @Test
    public void testUnEquals() {

        Village village = getVillageData();

        State state = TestHelper.getStateData();

        assertFalse(village.equals(state));
    }

    @Test
    public void testEqualsWithDifferentStateCode() {

        Village village = getVillageData();

        assertFalse(getVillageDataWithDifferentStateCode().equals(village));
    }

    @Test
    public void testEqualsWithDifferentDistrictCode() {

        Village village = getVillageData();

        assertFalse(getVillageDataWithDifferentDistrictCode().equals(village));
    }

    @Test
    public void testEqualsWithDifferentTalukaCode() {

        Village village = getVillageData();

        assertTrue(getVillageData().equals(village));
    }

    @Test
    public void testEqualsWithDifferentVillageCode() {

        Village village = getVillageData();

        assertFalse(getVillageDataWithDifferentVillageCode().equals(village));
    }

    @Test
    public void testEqualsWithSameVillageCode()
    {
        Village village = getVillageData();

        assertTrue(getVillageData().equals(village));
    }

    @Test
    public void testToString() {
        Village village = getVillageData();
        assertNotNull(village.toString());
    }

    @Test
    public void testHashCodeWithoutNull() {
        Village village = getVillageData();
        assertNotNull(village.hashCode());
    }

    @Test
    public void testHashCodeWithNull() {
        Village village = getVillageData();
        village.setStateCode(0L);
        village.setDistrictCode(0L);
        village.setTalukaCode(0L);
        village.setVillageCode(0L);
        assertTrue(0 == village.hashCode());
    }

    private Village getVillageData() {

        Village villageData = new Village();
        villageData.setStateCode(123L);
        villageData.setDistrictCode(456L);
        villageData.setTalukaCode(8L);
        villageData.setVillageCode(1002L);
        return villageData;
    }

    private Village getVillageDataWithDifferentStateCode() {

        Village village = new Village();
        village.setStateCode(1L);
        village.setDistrictCode(456L);
        village.setTalukaCode(8L);
        village.setVillageCode(1002L);

        return village;
    }

    private Village getVillageDataWithDifferentDistrictCode() {
        Village village = new Village();
        village.setStateCode(123L);
        village.setDistrictCode(4L);
        village.setTalukaCode(8L);
        village.setVillageCode(1002L);

        return village;
    }

    private Village getVillageDataWithDifferentTalukaCode() {

        Village village = new Village();
        village.setStateCode(123L);
        village.setDistrictCode(456L);
        village.setTalukaCode(9L);
        village.setVillageCode(1002L);

        return village;
    }

    private Village getVillageDataWithDifferentVillageCode() {

        Village village = new Village();
        village.setStateCode(123L);
        village.setDistrictCode(456L);
        village.setTalukaCode(8L);
        village.setVillageCode(1003L);

        return village;
    }

}
