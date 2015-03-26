package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.AbortionType;

public class AbortionTypeTest {


    @Test
    public  void shouldCheckValidAbortionType() {

        Assert.assertTrue(AbortionType.checkValidAbortionType(null));
        Assert.assertTrue(AbortionType.checkValidAbortionType("MTP>12 Weeks"));
        Assert.assertFalse(AbortionType.checkValidAbortionType("invalidType"));
    }
}
