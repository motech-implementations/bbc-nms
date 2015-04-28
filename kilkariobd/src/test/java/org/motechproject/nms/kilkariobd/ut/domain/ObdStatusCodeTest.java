package org.motechproject.nms.kilkariobd.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.domain.ObdStatusCode;

public class ObdStatusCodeTest {

    @Test
    public void shouldGetOdbStatusCodeByInteger() {

        Assert.assertEquals(ObdStatusCode.OBD_DNIS_IN_DND, ObdStatusCode.getByInteger(3001));
        Assert.assertEquals(ObdStatusCode.OBD_SUCCESS_CALL_CONNECTED, ObdStatusCode.getByInteger(1001));
        Assert.assertNull(ObdStatusCode.getByInteger(0));

    }
}
