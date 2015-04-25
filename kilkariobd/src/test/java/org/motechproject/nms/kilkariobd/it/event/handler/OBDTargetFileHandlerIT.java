package org.motechproject.nms.kilkariobd.it.event.handler;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallDetail;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;
import org.motechproject.nms.kilkariobd.repository.OutboundCallDetailDataService;
import org.motechproject.nms.kilkariobd.repository.OutboundCallFlowDataService;
import org.motechproject.nms.kilkariobd.service.OutboundCallDetailService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
import org.motechproject.nms.kilkariobd.settings.Settings;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class OBDTargetFileHandlerIT extends BasePaxIT {

    @Inject
    private OutboundCallFlowService callFlowService;

    @Inject
    private OutboundCallFlowDataService outboundCallFlowDataService;

    @Inject
    private OutboundCallDetailService callDetailService;

    @Inject
    private OutboundCallDetailDataService outboundCallDetailDataService;

    @After
    @Before
    public void setUp() {

        outboundCallFlowDataService.deleteAll();
        outboundCallDetailDataService.deleteAll();
    }

    @Test
    public void shouldProcessCDRDetails() {

        Settings settings = new Settings();
        OutboundCallFlow outboundCallFlow = new OutboundCallFlow();
        List<OutboundCallDetail> outboundCallDetails = new ArrayList<OutboundCallDetail>();

        URL url = this.getClass().getClassLoader().getResource("CDR_Detail_OBD_NMS1_2015041427090000");
        String filePath = url.getPath();

        settings.setObdFileLocalPath(filePath);

        outboundCallFlow.setObdFileName("CDR_Detail_OBD_NMS1_2015041427090000");
        outboundCallFlow.setStatus(CallFlowStatus.CDR_FILE_NOTIFICATION_RECEIVED);
        outboundCallFlow.setObdChecksum("9222d97dc5c87ed75d0c29e1307e60a2");
        outboundCallFlow.setCdrDetailRecordCount(1L);

        outboundCallFlowDataService.create(outboundCallFlow);

        outboundCallDetails = outboundCallDetailDataService.findRecordsByRequestId(123456L);

        Assert.assertEquals("9985167784",outboundCallDetails.get(0).getMsisdn());
    }
}
