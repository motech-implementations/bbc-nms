package org.motechproject.nms.kilkariobd.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.domain.CallDisconnectReason;
import org.motechproject.nms.kilkariobd.domain.OutboundCallDetail;

public class OutboundCallDetailTest {

    @Test
    public void shouldSetValuesInOutBoundCallDetail() {

        OutboundCallDetail outboundCallDetail = new OutboundCallDetail();

        outboundCallDetail.setCallId("callId");
        Assert.assertEquals("callId", outboundCallDetail.getCallId());

        outboundCallDetail.setAttemptNo(1);
        Assert.assertTrue(1 == outboundCallDetail.getAttemptNo());

        outboundCallDetail.setRequestId("requestId");
        Assert.assertEquals("requestId", outboundCallDetail.getRequestId());

        outboundCallDetail.setMsisdn("msisdn");
        Assert.assertEquals("msisdn", outboundCallDetail.getMsisdn());

        outboundCallDetail.setCallAnswerTime(1L);
        Assert.assertTrue(1L == outboundCallDetail.getCallAnswerTime());

        outboundCallDetail.setCallDisconnectReason(CallDisconnectReason.CONTENT_NOT_FOUND);
        Assert.assertEquals(CallDisconnectReason.CONTENT_NOT_FOUND, outboundCallDetail.getCallDisconnectReason());

        outboundCallDetail.setCallDurationInPulse(1L);
        Assert.assertTrue(1L == outboundCallDetail.getCallDurationInPulse());

        outboundCallDetail.setCallEndTime(3L);
        Assert.assertTrue(3L == outboundCallDetail.getCallEndTime());

        outboundCallDetail.setCallStartTime(2L);
        Assert.assertTrue(2L == outboundCallDetail.getCallStartTime());

        outboundCallDetail.setCallStatus(2);
        Assert.assertTrue(2 == outboundCallDetail.getCallStatus());

        outboundCallDetail.setCircleCode("CC");
        Assert.assertEquals("CC", outboundCallDetail.getCircleCode());

        outboundCallDetail.setContentFile("ContentFile");
        Assert.assertEquals("ContentFile", outboundCallDetail.getContentFile());

        outboundCallDetail.setLanguageLocationCode(1);
        Assert.assertTrue(1 == outboundCallDetail.getLanguageLocationCode());

        outboundCallDetail.setMsgPlayEndTime(6);
        Assert.assertTrue(6 == outboundCallDetail.getMsgPlayEndTime());

        outboundCallDetail.setWeekId("weekId");
        Assert.assertEquals("weekId", outboundCallDetail.getWeekId());

        outboundCallDetail.setOperatorCode("OC");
        Assert.assertEquals("OC", outboundCallDetail.getOperatorCode());

        outboundCallDetail.setPriority(6);
        Assert.assertTrue(6 == outboundCallDetail.getPriority());

        outboundCallDetail.setMsgPlayStartTime(1);
        Assert.assertTrue(1 == outboundCallDetail.getMsgPlayStartTime());

    }
}
