package org.motechproject.nms.masterdata.ut.domain;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.it.TestHelper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by abhishek on 11/3/15.
 */
public class HealthBlockTest {


    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void testEquals(){

        HealthBlock healthBlock = TestHelper.getHealthBlockData();

        assertTrue(healthBlock.equals(healthBlock));
    }

    @Test
    public void testUnEquals(){

        HealthBlock healthBlock = TestHelper.getHealthBlockData();

        State state = TestHelper.getStateData();

        assertFalse(healthBlock.equals(state));
    }

    @Test
    public void testEqualsWithDifferentStateCode(){

        HealthBlock healthBlock = TestHelper.getHealthBlockData();

        assertFalse(getHealthBlockDataWithDifferentStateCode().equals(healthBlock));
    }

    @Test
    public void testEqualsWithDifferentDistrictCode(){

        HealthBlock healthBlock = TestHelper.getHealthBlockData();

        assertFalse(getHealthBlockDataWithDifferentDistrictCode().equals(healthBlock));
    }

    @Test
    public void testEqualsWithDifferentTalukaCode(){

        HealthBlock healthBlock = TestHelper.getHealthBlockData();

        assertFalse(getHealthBlockDataWithDifferentTalukaCode().equals(healthBlock));
    }

    @Test
    public void testEqualsWithDifferentHealthBlockCode(){

        HealthBlock healthBlock = TestHelper.getHealthBlockData();

        assertFalse(getHealthBlockDataWithDifferentHealthBlockCode().equals(healthBlock));
    }

    private HealthBlock getHealthBlockDataWithDifferentStateCode() {

        HealthBlock healthBlock = new HealthBlock();
        healthBlock.setStateCode(1L);
        healthBlock.setDistrictCode(456L);
        healthBlock.setTalukaCode("8");
        healthBlock.setHealthBlockCode(1002L);

        return healthBlock;
    }

    private HealthBlock getHealthBlockDataWithDifferentDistrictCode() {

        HealthBlock healthBlock = new HealthBlock();
        healthBlock.setStateCode(123L);
        healthBlock.setDistrictCode(4L);
        healthBlock.setTalukaCode("8");
        healthBlock.setHealthBlockCode(1002L);

        return healthBlock;
    }

    private HealthBlock getHealthBlockDataWithDifferentTalukaCode() {

        HealthBlock healthBlock = new HealthBlock();
        healthBlock.setStateCode(123L);
        healthBlock.setDistrictCode(456L);
        healthBlock.setTalukaCode("9");
        healthBlock.setHealthBlockCode(1002L);

        return healthBlock;
    }

    private HealthBlock getHealthBlockDataWithDifferentHealthBlockCode() {

        HealthBlock healthBlock = new HealthBlock();
        healthBlock.setStateCode(123L);
        healthBlock.setDistrictCode(456L);
        healthBlock.setTalukaCode("8");
        healthBlock.setHealthBlockCode(1003L);

        return healthBlock;
    }

}
