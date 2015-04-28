package org.motechproject.nms.kilkariobd.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.domain.CallStatus;

public class CallStatusTest {

    @Test
    public void shouldGetStatusByInteger() {

        Assert.assertEquals(CallStatus.SUCCESS, CallStatus.getByInteger(1));
        Assert.assertEquals(CallStatus.FAILED, CallStatus.getByInteger(2));
        Assert.assertEquals(CallStatus.REJECTED, CallStatus.getByInteger(3));
        Assert.assertNull(CallStatus.getByInteger(4));
    }
}
