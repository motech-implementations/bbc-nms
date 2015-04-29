package org.motechproject.nms.kilkariobd.ut.service;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.nms.kilkariobd.builder.OutboundCallBuilder;
import org.motechproject.nms.kilkariobd.builder.RequestBuilder;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.domain.CallFlowStatus;
import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;
import org.motechproject.nms.kilkariobd.domain.OutboundCallFlow;
import org.motechproject.nms.kilkariobd.dto.request.CdrInfo;
import org.motechproject.nms.kilkariobd.dto.request.CdrNotificationRequest;
import org.motechproject.nms.kilkariobd.dto.request.FileProcessedStatusRequest;
import org.motechproject.nms.kilkariobd.repository.OutboundCallFlowDataService;
import org.motechproject.nms.kilkariobd.service.OutboundCallFlowService;
import org.motechproject.nms.kilkariobd.service.OutboundCallRequestService;
import org.motechproject.nms.kilkariobd.service.impl.OutboundCallFlowServiceImpl;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;

import static org.mockito.MockitoAnnotations.initMocks;

public class OutboundCallFlowServiceTest {

    @Mock
    OutboundCallFlowDataService callFlowDataService;

    @Mock
    OutboundCallRequestService outboundCallRequestService;

    @InjectMocks
    OutboundCallFlowService outboundCallFlowService = new OutboundCallFlowServiceImpl();

    @Before
    public void init() {

        initMocks(this);
    }

    @Test
    public void testHandleCdrNotificationWithCallFlowNull() {

        RequestBuilder requestBuilder = new RequestBuilder();
        CdrNotificationRequest cdrNotificationRequest = requestBuilder.buildCdrNotificationRequest("filename", null, null);

        Mockito.when(callFlowDataService.findRecordByFileName("filename")).thenReturn(null);

        try {
            outboundCallFlowService.handleCdrNotification(cdrNotificationRequest);
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) ex).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
            Assert.assertEquals(((DataValidationException) ex).getErrorDesc(), String.format(ErrorDescriptionConstants.INVALID_API_PARAMETER_DESCRIPTION, Constants.CDR_DETAIL_INFO));
        }
    }

    @Test
    public void testHandleCdrNotificationShouldUpdateCallFlow() {

        RequestBuilder requestBuilder = new RequestBuilder();
        CdrInfo cdrDetail = requestBuilder.buildCdrInfo("cdr_Detail_File", "checksum", 1L);
        CdrInfo cdrSummary = requestBuilder.buildCdrInfo("cdr_Summary_File", "checksum", 1L);
        CdrNotificationRequest cdrNotificationRequest = requestBuilder.buildCdrNotificationRequest("filename",cdrSummary,cdrDetail);

        OutboundCallBuilder outboundCallBuilder = new OutboundCallBuilder();
        OutboundCallFlow callFlow = outboundCallBuilder.buildCallFlow("ObdFileName", CallFlowStatus.CDR_FILES_PROCESSED,"summary_checksum", 1L, "detail_checksum", 1L, "OBD_checksum", 1L);
        OutboundCallFlow callFlowUpdated = new OutboundCallFlow();

        callFlowUpdated.setStatus(CallFlowStatus.CDR_FILE_NOTIFICATION_RECEIVED);
        callFlowUpdated.setCdrDetailChecksum(cdrNotificationRequest.getCdrDetail().getCdrChecksum());
        callFlowUpdated.setCdrDetailRecordCount(cdrNotificationRequest.getCdrDetail().getRecordsCount());
        callFlowUpdated.setCdrSummaryChecksum(cdrNotificationRequest.getCdrSummary().getCdrChecksum());
        callFlowUpdated.setCdrSummaryRecordCount(cdrNotificationRequest.getCdrSummary().getRecordsCount());

        Mockito.when(callFlowDataService.findRecordByFileName("filename")).thenReturn(callFlow);
        Mockito.doReturn(callFlowUpdated).when(callFlowDataService).update(callFlow);

        try {
            outboundCallFlowService.handleCdrNotification(cdrNotificationRequest);
            Assert.assertEquals(CallFlowStatus.CDR_FILE_NOTIFICATION_RECEIVED, callFlow.getStatus());
            Assert.assertEquals(callFlowUpdated.getCdrDetailChecksum(), callFlow.getCdrDetailChecksum());
            Assert.assertEquals(callFlowUpdated.getCdrDetailRecordCount(), callFlow.getCdrDetailRecordCount());
            Assert.assertEquals(callFlowUpdated.getCdrSummaryChecksum(), callFlow.getCdrSummaryChecksum());
            Assert.assertEquals(callFlowUpdated.getCdrSummaryRecordCount(), callFlow.getCdrDetailRecordCount());
        } catch (DataValidationException ex) {
        }
    }

    @Test
    public void testHandleFileProcessedStatusNotificationWithNullCallFlow() {

        FileProcessedStatusRequest fileProcessedStatusRequest = new FileProcessedStatusRequest(null, "filename", null, null);
        Mockito.when(callFlowDataService.findRecordByFileName("filename")).thenReturn(null);

        try {
            outboundCallFlowService.handleFileProcessedStatusNotification(fileProcessedStatusRequest);
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) ex).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
            Assert.assertEquals(((DataValidationException) ex).getErrorDesc(), String.format(ErrorDescriptionConstants.INVALID_API_PARAMETER_DESCRIPTION, Constants.FILE_NAME));
        }
    }

    @Test
    public void testHandleFileProcessedStatusNotificationShouldUpdateCallFlow() {

        FileProcessedStatusRequest fileProcessedStatusRequest = new FileProcessedStatusRequest(null, "filename", FileProcessingStatus.FILE_PROCESSED_SUCCESSFULLY, null);
        OutboundCallBuilder outboundCallBuilder = new OutboundCallBuilder();
        OutboundCallFlow callFlow = outboundCallBuilder.buildCallFlow("ObdFileName", CallFlowStatus.CDR_FILES_PROCESSED,"summary_checksum", 1L, "detail_checksum", 1L, "OBD_checksum", 1L);
        OutboundCallFlow callFlowUpdated = new OutboundCallFlow();

        callFlowUpdated.setStatus(CallFlowStatus.OUTBOUND_CALL_REQUEST_FILE_PROCESSED_AT_IVR);

        Mockito.when(callFlowDataService.findRecordByFileName("filename")).thenReturn(callFlow);
        Mockito.doReturn(callFlowUpdated).when(callFlowDataService).update(callFlow);
        Mockito.doNothing().when(outboundCallRequestService).deleteAll();

        try {
            outboundCallFlowService.handleFileProcessedStatusNotification(fileProcessedStatusRequest);
            Assert.assertEquals(callFlowUpdated.getStatus(), callFlow.getStatus());

        } catch (DataValidationException ex) {

        }
    }

    @Test
    public void shouldUpdateCallFlowStatusIfFoundByFileName() {

        OutboundCallFlow todayCallFlow = new OutboundCallFlow();

        todayCallFlow.setStatus(CallFlowStatus.CDR_FILE_NOTIFICATION_RECEIVED);

        Mockito.when(callFlowDataService.findRecordByFileName("filename")).thenReturn(todayCallFlow);
        outboundCallFlowService.updateCallFlowStatus("filename",CallFlowStatus.CDR_FILES_PROCESSED);

        Assert.assertEquals(CallFlowStatus.CDR_FILES_PROCESSED, todayCallFlow.getStatus());
    }

    @Test
    public void shouldUpdateCallFlowStatusIfNotFoundByFileName() {

        Mockito.when(callFlowDataService.findRecordByFileName("filename")).thenReturn(null);

        Assert.assertNull(outboundCallFlowService.updateCallFlowStatus("filename", CallFlowStatus.CDR_FILES_PROCESSED));
    }

    @Test
    public void shouldUpdateObdChecksumAndRecordCountIfFoundByFileName() {

        OutboundCallFlow todayCallFlow = new OutboundCallFlow();

        todayCallFlow.setObdChecksum("checksum");
        todayCallFlow.setObdRecordCount(1L);

        Mockito.when(callFlowDataService.findRecordByFileName("filename")).thenReturn(todayCallFlow);
        outboundCallFlowService.updateChecksumAndRecordCount("filename", "obdChecksum", 2L);

        Assert.assertEquals("obdChecksum", todayCallFlow.getObdChecksum());
        Assert.assertTrue(2L == todayCallFlow.getObdRecordCount());
    }

    @Test
    public void shouldUpdateObdChecksumAndRecordCountIfNotFoundByFileName() {

        Mockito.when(callFlowDataService.findRecordByFileName("filename")).thenReturn(null);
        Assert.assertNull(outboundCallFlowService.updateChecksumAndRecordCount("filename", "obdChecksum", 2L));
    }


}
