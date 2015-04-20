package org.motechproject.nms.mobileacademy.service.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.mobileacademy.commons.MobileAcademyConstants;
import org.motechproject.nms.mobileacademy.domain.CallDetail;
import org.motechproject.nms.mobileacademy.domain.ContentLog;
import org.motechproject.nms.mobileacademy.domain.FlwUsageDetail;
import org.motechproject.nms.mobileacademy.dto.CallDetailsRequest;
import org.motechproject.nms.mobileacademy.dto.ContentLogRequest;
import org.motechproject.nms.mobileacademy.repository.CallDetailDataService;
import org.motechproject.nms.mobileacademy.repository.ContentLogDataService;
import org.motechproject.nms.mobileacademy.repository.FlwUsageDetailDataService;
import org.motechproject.nms.mobileacademy.service.UserDetailsService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class UserDetailsServiceIT extends BasePaxIT {

    @Inject
    private FrontLineWorkerService frontLineWorkerService;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private FlwUsageDetailDataService flwUsageDetailDataService;

    @Inject
    private CallDetailDataService callDetailDataService;

    @Inject
    private ContentLogDataService contentLogDataService;

    @Before
    public void setupData() {
        clearData();
        if (frontLineWorkerService.getFlwBycontactNo("9990632901") == null) {
            // create FLW record
            FrontLineWorker frontLineWorker = new FrontLineWorker();
            frontLineWorker.setCircleCode("DL");
            frontLineWorker.setLanguageLocationCodeId(29);
            frontLineWorker.setContactNo("9990632901");
            frontLineWorker.setOperatorCode("AL");
            frontLineWorker.setStatus(Status.ACTIVE);
            frontLineWorkerService.createFrontLineWorker(frontLineWorker);
        }
    }

    /**
     * test Save Call Details Success
     */
    @Test
    public void testSaveCallDetailsSuccess() {
        CallDetailsRequest callDetailRequest = new CallDetailsRequest();
        callDetailRequest.setCallingNumber("9990632901");
        callDetailRequest.setCallId("12345678");
        callDetailRequest.setOperator("AL");
        callDetailRequest.setCircle("DL");
        callDetailRequest.setCallStartTime("1429166000");// start time
        callDetailRequest.setCallEndTime("1429171000");
        callDetailRequest.setCallDurationInPulses("20");
        callDetailRequest.setEndOfUsagePromptCounter("3");
        callDetailRequest.setCallStatus("1");
        callDetailRequest.setCallDisconnectReason("6");
        // content
        List<ContentLogRequest> contentList = new ArrayList<>();
        // course start record
        ContentLogRequest contentLogRequestFirst = new ContentLogRequest();
        contentLogRequestFirst.setType("lesson");
        contentLogRequestFirst.setCompletionFlag("true");
        contentLogRequestFirst.setContentFile("ch1.wav");
        contentLogRequestFirst
                .setContentName(MobileAcademyConstants.COURSE_START_CONTENT);
        contentLogRequestFirst.setCorrectAnswerReceived("true");
        contentLogRequestFirst.setStartTime("1429167000");
        contentLogRequestFirst.setEndTime("1429169000");
        // course end record
        ContentLogRequest contentLogRequestLast = new ContentLogRequest();
        contentLogRequestLast.setType("question");
        contentLogRequestLast.setCompletionFlag("true");
        contentLogRequestLast.setContentFile("ch2.wav");
        contentLogRequestLast
                .setContentName(MobileAcademyConstants.COURSE_END_CONTENT);
        contentLogRequestLast.setCorrectAnswerReceived("true");
        contentLogRequestLast.setStartTime("1429168000");
        contentLogRequestLast.setEndTime("1429170000");

        contentList.add(contentLogRequestFirst);
        contentList.add(contentLogRequestLast);
        callDetailRequest.setContent(contentList);

        try {
            userDetailsService.saveCallDetails(callDetailRequest);
            FlwUsageDetail flwUsageDetail = flwUsageDetailDataService.retrieve(
                    "msisdn", 9990632901l);
            CallDetail callDetail = callDetailDataService.retrieve("callId",
                    12345678);
            ContentLog contentLogFirstRecord = contentLogDataService.retrieve(
                    "contentName",
                    MobileAcademyConstants.COURSE_START_CONTENT.toUpperCase());
            ContentLog contentLogLastRecord = contentLogDataService.retrieve(
                    "contentName",
                    MobileAcademyConstants.COURSE_END_CONTENT.toUpperCase());
            // assert usage table data
            assertTrue(3 == flwUsageDetail.getEndOfUsagePromptCounter());
            assertTrue(20 == flwUsageDetail.getCurrentUsageInPulses());
            assertTrue(9990632901l == flwUsageDetail.getMsisdn());
            assertTrue(1429166000l == (flwUsageDetail.getLastAccessTime()
                    .getMillis() / 1000));
            assertTrue(1429167000l == (flwUsageDetail.getCourseStartDate()
                    .getMillis() / 1000));
            assertTrue(1429170000l == (flwUsageDetail.getCourseEndDate()
                    .getMillis() / 1000));

            // assert call detail table data
            assertTrue(12345678 == callDetail.getCallId());
            assertTrue(9990632901l == callDetail.getMsisdn());
            assertEquals(flwUsageDetail.getFlwId(), callDetail.getFlwId());
            assertEquals("AL", callDetail.getOperator());
            assertEquals("DL", callDetail.getCircle());
            assertTrue(1 == callDetail.getCallStatus());
            assertTrue(6 == callDetail.getCallDisconnectReason());
            assertTrue(1429166000l == (callDetail.getCallStartTime()
                    .getMillis() / 1000));
            assertTrue(1429171000l == (callDetail.getCallEndTime().getMillis() / 1000));

            // assert content log records
            assertTrue(29 == contentLogFirstRecord.getLanguageLocationCode());
            assertTrue(12345678 == contentLogFirstRecord.getCallId());
            assertTrue(contentLogFirstRecord.getCompletionFlag());
            assertEquals("ch1.wav", contentLogFirstRecord.getContentFile());
            assertEquals(
                    MobileAcademyConstants.COURSE_START_CONTENT.toUpperCase(),
                    contentLogFirstRecord.getContentName());
            assertEquals("lesson".toUpperCase(),
                    contentLogFirstRecord.getType());
            assertTrue(contentLogFirstRecord.getCorrectAnswerReceived());
            assertTrue(1429167000l == (contentLogFirstRecord
                    .getCourseStartDate().getMillis() / 1000));
            assertTrue(null == contentLogFirstRecord.getCourseEndDate());
            assertTrue(1429167000l == (contentLogFirstRecord.getStartTime()
                    .getMillis() / 1000));
            assertTrue(1429169000l == (contentLogFirstRecord.getEndTime()
                    .getMillis() / 1000));

            assertTrue(29 == contentLogLastRecord.getLanguageLocationCode());
            assertTrue(12345678 == contentLogLastRecord.getCallId());
            assertTrue(contentLogLastRecord.getCompletionFlag());
            assertEquals("ch2.wav", contentLogLastRecord.getContentFile());
            assertEquals(
                    MobileAcademyConstants.COURSE_END_CONTENT.toUpperCase(),
                    contentLogLastRecord.getContentName());
            assertEquals("question".toUpperCase(),
                    contentLogLastRecord.getType());
            assertTrue(contentLogLastRecord.getCorrectAnswerReceived());
            assertTrue(1429167000l == (contentLogLastRecord
                    .getCourseStartDate().getMillis() / 1000));
            assertTrue(1429170000l == (contentLogLastRecord.getCourseEndDate()
                    .getMillis() / 1000));
            assertTrue(1429168000l == (contentLogLastRecord.getStartTime()
                    .getMillis() / 1000));
            assertTrue(1429170000l == (contentLogLastRecord.getEndTime()
                    .getMillis() / 1000));

        } catch (DataValidationException e) {
            assertTrue(false);
        }
    }

    @After
    public void clearData() {
        contentLogDataService.deleteAll();
        callDetailDataService.deleteAll();
        flwUsageDetailDataService.deleteAll();
    }

}
