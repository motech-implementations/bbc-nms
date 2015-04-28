package org.motechproject.nms.kilkariobd.builder;

import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;

public class OutboundCallBuilder {

    public OutboundCallFlow buildCallFlow(String obdFileName, CallFlowStatus status, String summaryChecksum,
                                                  Long summaryRecordCount, String detailChecksum, Long detailRecordCount,
                                                  String obdChecksum, Long obdRecordCount) {
        OutboundCallFlow callFlow = new OutboundCallFlow();
        callFlow.setObdFileName(obdFileName);
        callFlow.setStatus(status);
        callFlow.setObdChecksum(obdChecksum);
        callFlow.setCdrDetailChecksum(detailChecksum);
        callFlow.setCdrDetailRecordCount(detailRecordCount);
        callFlow.setCdrSummaryChecksum(summaryChecksum);
        callFlow.setCdrSummaryRecordCount(summaryRecordCount);
        callFlow.setObdRecordCount(obdRecordCount);
        return callFlow;
    }
}
