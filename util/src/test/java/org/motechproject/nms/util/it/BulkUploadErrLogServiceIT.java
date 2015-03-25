package org.motechproject.nms.util.it;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.domain.RecordType;
import org.motechproject.nms.util.repository.BulkUploadErrorDataService;
import org.motechproject.nms.util.repository.BulkUploadStatusDataService;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * Test class to test functionality exposed by BulkUploadStatusService
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class BulkUploadErrLogServiceIT extends BasePaxIT {

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    private BulkUploadStatusDataService bulkUploadStatusDataService;

    @Inject
    private BulkUploadErrorDataService bulkUploadErrorDataService;

    private Logger logger = LoggerFactory.getLogger(BulkUploadErrLogServiceIT.class);

    @Before
    public void setUp() {
        assertNotNull(bulkUploadStatusDataService);
        assertNotNull(bulkUploadErrLogService);
        assertNotNull(bulkUploadErrorDataService);
    }

    /**
     * This test case tests the add method of BulkUploadStatusService for
     * successfully adding a record in db after a bulk upload is complete.
     * @throws Exception
     */
    @Test
    public void shouldAddBulkUploadStatus() throws Exception {

        String bulkUploadFileName = "csv-import.someFileName";
        Integer numberOfFailedRecords = 82;
        Integer numberOfSuccessfulRecords = 3;
        String userName = "abcdef";
        DateTime timeOfUpload = new DateTime();

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        bulkUploadStatus.setBulkUploadFileName(bulkUploadFileName);
        bulkUploadStatus.setNumberOfFailedRecords(numberOfFailedRecords);
        bulkUploadStatus.setNumberOfSuccessfulRecords(numberOfSuccessfulRecords);
        bulkUploadStatus.setUploadedBy(userName);
        bulkUploadStatus.setTimeOfUpload(timeOfUpload);

        bulkUploadErrLogService.writeBulkUploadProcessingSummary(bulkUploadStatus);

        List<BulkUploadStatus> list = bulkUploadStatusDataService.retrieveAll();
        assertNotNull(list);
        for(BulkUploadStatus status : list) {
            String retrievedBulkUploadFileName = status.getBulkUploadFileName();
            DateTime retrievedTimeOfUpload = status.getTimeOfUpload();
            if(retrievedBulkUploadFileName.equalsIgnoreCase(bulkUploadFileName)
                    && retrievedTimeOfUpload == timeOfUpload) {
                Integer retrievedCountOfFailedRecords = status.getNumberOfFailedRecords();
                Integer retrievedCountOfSuccessfulRecords = status.getNumberOfSuccessfulRecords();
                String retrievedUserName = status.getUploadedBy();

                Assert.assertEquals(numberOfFailedRecords, retrievedCountOfFailedRecords);
                Assert.assertEquals(numberOfSuccessfulRecords, retrievedCountOfSuccessfulRecords);
                Assert.assertEquals(userName, retrievedUserName);

            }
        }
        bulkUploadStatusDataService.deleteAll();
    }

    /**
     * This test case tests the add method of BulkUploadStatusService for
     * successfully adding a record in db after a bulk upload is complete.
     * @throws Exception
     */
    @Test
    public void shouldAddBulkUploadErrRecord() throws Exception {
        bulkUploadErrorDataService.deleteAll();
        String bulkUploadFileName = "csv-import.someFileName";
        DateTime timeOfUpload = new DateTime();
        String recordDetails = "Some Record Details";

        BulkUploadError bulkUploadError = new BulkUploadError();
        bulkUploadError.setTimeOfUpload(timeOfUpload);
        bulkUploadError.setRecordDetails(recordDetails);
        bulkUploadError.setCsvName(bulkUploadFileName);
        bulkUploadError.setErrorCategory(ErrorCategoryConstants.GENERAL_EXCEPTION);
        bulkUploadError.setErrorDescription(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
        bulkUploadError.setRecordType(RecordType.CIRCLE);
        bulkUploadErrLogService.writeBulkUploadErrLog(bulkUploadError);

        List<BulkUploadError> list = bulkUploadErrorDataService.retrieveAll();
        assertNotNull(list);
        for(BulkUploadError error : list) {
            String retrievedBulkUploadFileName = error.getCsvName();
            DateTime retrievedTimeOfUpload = error.getTimeOfUpload();
            String retrievedRecordDetails = error.getRecordDetails();

            if(retrievedBulkUploadFileName.equalsIgnoreCase(bulkUploadFileName)
                    && retrievedTimeOfUpload == timeOfUpload
                    && retrievedRecordDetails == recordDetails) {

                String retrievedErrorCategory = error.getErrorCategory();
                String retrievedErrorDesc = error.getErrorDescription();
                RecordType retrievedRecordType = error.getRecordType();

                Assert.assertEquals(RecordType.CIRCLE, retrievedRecordType);
                Assert.assertEquals(ErrorCategoryConstants.GENERAL_EXCEPTION, retrievedErrorCategory);
                Assert.assertEquals(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION, retrievedErrorDesc);

            }
        }
        bulkUploadErrorDataService.deleteAll();
    }
}
