package org.motechproject.nms.kilkariobd.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.domain.CallStatus;

public class CallStatusTest {

    @Test
    public void shouldGetStatusByInteger() {

        Assert.assertNotNull(CallStatus.getByInteger(1));
        Assert.assertNull(CallStatus.getByInteger(4));
    }
}
