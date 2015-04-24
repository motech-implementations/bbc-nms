package org.motechproject.nms.kilkariobd.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;

public class OutboundCallFlowTest {

    @Test
    public void shouldSetValuesInOutboundCallFlow() {

        OutboundCallFlow outboundCallFlow = new OutboundCallFlow();

        outboundCallFlow.setCdrDetailChecksum("detailChecksum");
        Assert.assertEquals("detailChecksum", outboundCallFlow.getCdrDetailChecksum());

        outboundCallFlow.setCdrDetailRecordCount(1L);
        Assert.assertTrue(1L == outboundCallFlow.getCdrDetailRecordCount());

        outboundCallFlow.setCdrSummaryChecksum("summaryChecksum");
        Assert.assertEquals("summaryChecksum", outboundCallFlow.getCdrSummaryChecksum());

        outboundCallFlow.setCdrSummaryRecordCount(1L);
        Assert.assertTrue(1L == outboundCallFlow.getCdrSummaryRecordCount());

        outboundCallFlow.setObdChecksum("OBDChecksum");
        Assert.assertEquals("OBDChecksum", outboundCallFlow.getObdChecksum());

        outboundCallFlow.setObdFileName("OBDFileName");
        Assert.assertEquals("OBDFileName", outboundCallFlow.getObdFileName());

        outboundCallFlow.setObdRecordCount(2L);
        Assert.assertTrue(2L == outboundCallFlow.getObdRecordCount());

        outboundCallFlow.setStatus(CallFlowStatus.CDR_FILE_NOTIFICATION_RECEIVED);
        Assert.assertEquals(CallFlowStatus.CDR_FILE_NOTIFICATION_RECEIVED, outboundCallFlow.getStatus());
    }


}
