package org.motechproject.nms.kilkariobd.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.domain.CallDisconnectReason;

public class CallDisconnectReasonTest {

    @Test
    public void shouldReturnCallDisconnectReason() {

        Assert.assertNotNull(CallDisconnectReason.getByString("Normal Drop"));
        Assert.assertNull(CallDisconnectReason.getByString("Wrong String"));
    }
}
