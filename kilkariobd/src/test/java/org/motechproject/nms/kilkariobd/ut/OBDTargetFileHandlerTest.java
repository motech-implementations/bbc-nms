package org.motechproject.nms.kilkariobd.ut;


import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.nms.kilkariobd.builder.OutboundCallBuilder;
import org.motechproject.nms.kilkariobd.client.HttpClient;
import org.motechproject.nms.kilkariobd.client.ex.CDRFileProcessingFailedException;
import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;
import org.motechproject.nms.kilkariobd.event.handler.OBDTargetFileHandler;
import org.motechproject.nms.kilkariobd.repository.OutboundCallFlowDataService;
import org.motechproject.nms.kilkariobd.service.OutboundCallDetailService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;

import java.lang.reflect.Method;
import java.net.URL;

import static org.mockito.MockitoAnnotations.initMocks;

public class OBDTargetFileHandlerTest extends TestCase {

    @Mock
    private OutboundCallFlowService callFlowService;

    @Mock
    private OutboundCallDetailService callDetailService;

    @Mock
    private OutboundCallFlowDataService outboundCallFlowDataService;

    @Mock
    private HttpClient httpClient;

    @InjectMocks
    OBDTargetFileHandler handler = new OBDTargetFileHandler();

    @Before
    public void init() {
        initMocks(this);
    }

    @Ignore
    public void testProcessCDRDetailThrowsCDRFileProcessingFailedException() {
        init();
        Method method = null;
        OutboundCallBuilder callBuilder = new OutboundCallBuilder();
        DateTime date = DateTime.now().withTimeAtStartOfDay();

        OutboundCallFlow todayCallFlow = callBuilder.buildCallFlow(
                null, CallFlowStatus.CDR_FILE_NOTIFICATION_RECEIVED, null, null, null, null, null, null);

        OutboundCallFlow oldCallFlow = callBuilder.buildCallFlow(null, null, null, null, null, 2L, null, null);

        OutboundCallFlow todayCallFlowUpdated = callBuilder.buildCallFlow(
                null, CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED, null, null, null, null, null, null);

        URL url = this.getClass().getClassLoader().getResource("CDR_Detail_OBD_NMS1_2015041427090000.csv");
        String filePath = url.getPath();

        Mockito.when(callFlowService.findRecordByCreationDate(date)).thenReturn(todayCallFlow);
        Mockito.when(callFlowService.findRecordByFileName(filePath)).thenReturn(oldCallFlow);
        Mockito.doReturn(todayCallFlowUpdated).when(callFlowService).update(todayCallFlow);
        //Mockito.doNothing().when(httpClient).notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_RECORDCOUNT_ERROR, filePath);


        try {
            method = handler.getClass().getDeclaredMethod("processCDRDetail",String.class);
            method.setAccessible(true);
            method.invoke(handler, filePath);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch(Exception ex) {
            Assert.assertEquals("CDR_DETAIL_PROCESSING_FAILED", todayCallFlow.getStatus());
            Assert.assertTrue(ex instanceof CDRFileProcessingFailedException);
            Assert.assertEquals(((CDRFileProcessingFailedException) ex).getErrorCode(), FileProcessingStatus.FILE_RECORDCOUNT_ERROR);
        }

    }

}
