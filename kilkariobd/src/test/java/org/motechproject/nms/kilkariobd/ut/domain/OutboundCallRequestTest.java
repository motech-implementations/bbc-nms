package org.motechproject.nms.kilkariobd.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.domain.OutboundCallRequest;

public class OutboundCallRequestTest {

    @Test
    public void shouldSetValuesInOutboundCallRequest() {

        OutboundCallRequest outboundCallRequest = new OutboundCallRequest();

        outboundCallRequest.setCallFlowURL("callFlowURL");
        Assert.assertEquals("callFlowURL", outboundCallRequest.getCallFlowURL());

        outboundCallRequest.setCircleCode("CC");
        Assert.assertEquals("CC", outboundCallRequest.getCircleCode());

        outboundCallRequest.setCli("cli");
        Assert.assertEquals("cli", outboundCallRequest.getCli());

        outboundCallRequest.setContentFileName("contentFileName");
        Assert.assertEquals("contentFileName", outboundCallRequest.getContentFileName());

        outboundCallRequest.setLanguageLocationCode(2);
        Assert.assertTrue(2 == outboundCallRequest.getLanguageLocationCode());

        outboundCallRequest.setMsisdn("msisdn");
        Assert.assertEquals("msisdn", outboundCallRequest.getMsisdn());

        outboundCallRequest.setPriority(4);
        Assert.assertTrue(4 == outboundCallRequest.getPriority());

        outboundCallRequest.setRequestId("requestId");
        Assert.assertEquals("requestId", outboundCallRequest.getRequestId());

        outboundCallRequest.setServiceId("serviceId");
        Assert.assertEquals("serviceId", outboundCallRequest.getServiceId());

        outboundCallRequest.setWeekId("weekId");
        Assert.assertEquals("weekId", outboundCallRequest.getWeekId());

    }
}
