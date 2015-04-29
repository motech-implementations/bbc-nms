package org.motechproject.nms.kilkariobd.ut;


import junit.framework.TestCase;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.nms.kilkari.service.ContentUploadService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkariobd.builder.OutboundCallBuilder;
import org.motechproject.nms.kilkari.domain.DeactivationReason;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.kilkariobd.client.HttpClient;
import org.motechproject.nms.kilkariobd.client.ex.CDRFileProcessingFailedException;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.domain.*;
import org.motechproject.nms.kilkariobd.event.handler.OBDTargetFileHandler;
import org.motechproject.nms.kilkariobd.repository.OutboundCallFlowDataService;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.motechproject.nms.kilkariobd.service.OutboundCallDetailService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
import org.motechproject.nms.kilkariobd.service.OutboundCallRequestService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.server.config.SettingsFacade;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.nms.kilkariobd.commons.Constants.CDR_DETAIL_FILE_PREFIX;

public class OBDTargetFileHandlerTest extends TestCase {

    @Mock
    private HttpClient httpClient;

    @Mock
    private OutboundCallRequestService requestService;

    @Mock
    private ConfigurationService configurationService;

    @Mock
    private LanguageLocationCodeService llcService;

    @Mock
    private OutboundCallFlowService callFlowService;

    @Mock
    private OutboundCallDetailService callDetailService;

    @Mock
    private ContentUploadService contentUploadService;

    @Mock
    private SettingsFacade kilkariObdSettings;

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private MotechSchedulerService motechSchedulerService;

    @InjectMocks
    OBDTargetFileHandler handler = new OBDTargetFileHandler(requestService, configurationService,
            llcService, callFlowService, subscriptionService, callDetailService, contentUploadService,
            kilkariObdSettings, motechSchedulerService);

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void testProcessCDRDetailThrowsCDRFileProcessingFailedException() {

        init();

        Method method = null;
        OutboundCallFlow oldCallFlow = new OutboundCallFlow();
        OutboundCallFlow todayCallFlowUpdated = new OutboundCallFlow();

        URL url = this.getClass().getClassLoader().getResource("CDR_Detail_OBD_NMS1_2015041427090000.csv");
        String filePath = url.getPath();

        todayCallFlowUpdated.setStatus(CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED);
        oldCallFlow.setCdrDetailRecordCount(2L);

        String failureReason = String.format(Constants.INVALID_RECORD_COUNT,
                oldCallFlow.getCdrDetailRecordCount(), 1, CDR_DETAIL_FILE_PREFIX + "oldObdFileName");

        Mockito.doReturn(todayCallFlowUpdated).when(callFlowService).updateCallFlowStatus("obdFileName", CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED);
        Mockito.when(callFlowService.findRecordByFileName("oldObdFileName")).thenReturn(oldCallFlow);
        Mockito.doNothing().when(httpClient).notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_RECORDCOUNT_ERROR, filePath, failureReason);


        try {
            method = handler.getClass().getDeclaredMethod("processCDRDetail", String.class, String.class, String.class);
            method.setAccessible(true);
            method.invoke(handler, "obdFileName", "oldObdFileName", filePath);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Assert.assertTrue(ex.getCause() instanceof CDRFileProcessingFailedException);
        }
    }


    @Test
    public void testProcessCDRDetailThrowsFileChecksumError() {

        init();

        Method method = null;
        OutboundCallFlow oldCallFlow = new OutboundCallFlow();
        OutboundCallFlow todayCallFlowUpdated = new OutboundCallFlow();

        URL url = this.getClass().getClassLoader().getResource("CDR_Detail_OBD_NMS1_2015041427090000.csv");
        String filePath = url.getPath();

        todayCallFlowUpdated.setStatus(CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED);
        oldCallFlow.setCdrDetailRecordCount(1L);
        oldCallFlow.setCdrDetailChecksum("9222d97dc5c87ed75d0c29e1307e60");

        String failureReason = String.format(Constants.INVALID_CHECKSUM,
                oldCallFlow.getCdrDetailChecksum(), "9222d97dc5c87ed75d0c29e1307e60a2", CDR_DETAIL_FILE_PREFIX + "oldObdFileName");

        Mockito.when(callFlowService.findRecordByFileName("oldObdFileName")).thenReturn(oldCallFlow);
        Mockito.doReturn(todayCallFlowUpdated).when(callFlowService).updateCallFlowStatus("obdFileName", CallFlowStatus.CDR_DETAIL_PROCESSING_FAILED);
        Mockito.doNothing().when(httpClient).notifyCDRFileProcessedStatus(FileProcessingStatus.FILE_RECORDCOUNT_ERROR, filePath, failureReason);


        try {
            method = handler.getClass().getDeclaredMethod("processCDRDetail", String.class, String.class, String.class);
            method.setAccessible(true);
            method.invoke(handler, "obdFileName", "oldObdFileName", filePath);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Assert.assertTrue(ex.getCause() instanceof CDRFileProcessingFailedException);
        }
    }


    @Test
    public void testProcessCDRDetailShouldCreateCallDetail() {

        init();

        Method method = null;
        OutboundCallFlow oldCallFlow = new OutboundCallFlow();
        OutboundCallDetail callDetail = getCallDetail();
        OutboundCallDetail callDetailReturn = getCallDetail();

        URL url = this.getClass().getClassLoader().getResource("CDR_Detail_OBD_NMS1_2015041427090000.csv");
        String filePath = url.getPath();

        oldCallFlow.setCdrDetailRecordCount(1L);
        oldCallFlow.setCdrDetailChecksum("9222d97dc5c87ed75d0c29e1307e60a2");

        Mockito.when(callFlowService.findRecordByFileName("oldObdFileName")).thenReturn(oldCallFlow);
        Mockito.doReturn(callDetailReturn).when(callDetailService).create(callDetail);

        try {
            method = handler.getClass().getDeclaredMethod("processCDRDetail", String.class, String.class, String.class);
            method.setAccessible(true);
            method.invoke(handler, "obdFileName", "oldObdFileName", filePath);

            Assert.assertEquals(callDetail.getAttemptNo(), callDetailReturn.getAttemptNo());

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception ex) {

        }
    }

    @Test
    public void testProcessCDRDetailShouldThrowFileNotFoundException() {

        init();
        Method method = null;

        try {
            method = handler.getClass().getDeclaredMethod("processCDRDetail", String.class, String.class, String.class);
            method.setAccessible(true);
            method.invoke(handler, "obdFileName", "oldObdFileName", "filePath");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Assert.assertTrue(ex.getCause() instanceof FileNotFoundException);
        }
    }

    @Test
    public void testProcessCDRSummaryShouldThrowFileNotFoundException() {

        init();
        Method method = null;
        Configuration configuration = new Configuration();

        try {
            method = handler.getClass().getDeclaredMethod("processCDRSummaryCSV", String.class, String.class, String.class, Configuration.class);
            method.setAccessible(true);
            method.invoke(handler, "obdFileName", "oldObdFileName", "filePath", configuration);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Assert.assertTrue(ex.getCause() instanceof FileNotFoundException);
        }
    }

    @Test
    public void testProcessCDRSummaryShouldDeactivateSubscription() {

        init();

        Method method = null;
        OutboundCallFlow oldCallFlow = new OutboundCallFlow();
        Configuration configuration = new Configuration();

        URL url = this.getClass().getClassLoader().getResource("Cdr_Summary_OBD_NMS1_20150127090000.csv");
        String filePath = url.getPath();

        oldCallFlow.setCdrSummaryRecordCount(1L);
        oldCallFlow.setCdrSummaryChecksum("c15e3d3785e43caf6ec71f8c4de70251");

        Mockito.when(callFlowService.findRecordByFileName("oldObdFileName")).thenReturn(oldCallFlow);
        Mockito.when(subscriptionService.deactivateSubscription(123456L,DeactivationReason.INVALID_MSISDN)).thenReturn(true);

        try {
            method = handler.getClass().getDeclaredMethod("processCDRSummaryCSV", String.class, String.class, String.class, Configuration.class);
            method.setAccessible(true);
            method.invoke(handler, "obdFileName", "oldObdFileName", filePath, configuration);

            Mockito.verify(subscriptionService).deactivateSubscription(123456L,DeactivationReason.INVALID_MSISDN);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception ex) {

        }
    }



    public OutboundCallDetail getCallDetail() {
        OutboundCallDetail callDetail = new OutboundCallDetail();

        callDetail.setRequestId("123456");
        callDetail.setMsisdn("9985167784");
        callDetail.setCallId("142712171090954");
        callDetail.setAttemptNo(1);
        callDetail.setCallStartTime(1427121711L);
        callDetail.setCallAnswerTime(1427121711L);
        callDetail.setCallEndTime(1427121720L);
        callDetail.setCallDurationInPulse(2L);
        callDetail.setCallStatus(4);
        callDetail.setLanguageLocationCode(11);
        callDetail.setContentFile("W11_1.wav");
        callDetail.setMsgPlayEndTime(Integer.parseInt("1427121719"));
        callDetail.setMsgPlayStartTime(Integer.parseInt("1427121712"));
        callDetail.setCircleCode("AP");
        callDetail.setOperatorCode("D");
        callDetail.setPriority(1);
        callDetail.setCallDisconnectReason(CallDisconnectReason.NORMAL_DROP);
        callDetail.setWeekId("22");

        return callDetail;
    }
}