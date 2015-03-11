package org.motechproject.nms.util.it;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.motechproject.nms.util.repository.BulkUploadStatusDataService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
/**
 * Test class to test functionality exposed by BulkUploadStatusService
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class BulkUploadStatusServiceIT extends BasePaxIT {

//    @Inject
//    private BulkUploadStatusService bulkUploadStatusService;

    @Inject
    private BulkUploadStatusDataService bulkUploadStatusDataService;

    /**
     * This test case tests the add method of BulkUploadStatusService for
     * successfully adding a record in db after a bulk upload is complete.
     * @throws Exception
     */
    @Ignore
    public void shouldAddBulkUploadStatus() throws Exception {
               assertNotNull(bulkUploadStatusDataService);
//
//        BulkUploadError bulkUploadError = new BulkUploadError();
//        String logFileName = bulkUploadError.createBulkUploadErrLogFileName("CircleCsv");
//        String bulkUploadFileName = "csv-import.someFileName";
//        Integer numberOfFailedRecords = 82;
//        Integer numberOfSuccessfulRecords = 3;
//        String logFileServerIP = "10.203.3.63";
//        String userName = "abcdef";
//
//        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
//        bulkUploadStatus.setLogFileServerIp(logFileServerIP);
//        bulkUploadStatus.setLogFileName(logFileName);
//        bulkUploadStatus.setBulkUploadFileName(bulkUploadFileName);
//        bulkUploadStatus.setNumberOfFailedRecords(numberOfFailedRecords);
//        bulkUploadStatus.setNumberOfSuccessfulRecords(numberOfSuccessfulRecords);
//        bulkUploadStatus.setUploadedBy(userName);
//        bulkUploadStatusService.add(bulkUploadStatus);
//
//        List<BulkUploadStatus> list = bulkUploadStatusDataService.retrieveAll();
//        assertNotNull(list);
//        for(BulkUploadStatus status : list) {
//            String retrievedLogFileName = status.getLogFileName();
//            if(retrievedLogFileName.equalsIgnoreCase(logFileName)) {
//                String retrievedBulkUploadFileName = status.getBulkUploadFileName();
//                String retrievedLogFileServerIP = status.getLogFileServerIp();
//                Integer retrievedCountOfFailedRecords = status.getNumberOfFailedRecords();
//                Integer retrievedCountOfSuccessfulRecords = status.getNumberOfSuccessfulRecords();
//                String retrievedUserName = status.getUploadedBy();
//
//                Assert.assertEquals(bulkUploadFileName, retrievedBulkUploadFileName);
//                Assert.assertEquals(logFileServerIP, retrievedLogFileServerIP);
//                Assert.assertEquals(numberOfFailedRecords, retrievedCountOfFailedRecords);
//                Assert.assertEquals(numberOfSuccessfulRecords, retrievedCountOfSuccessfulRecords);
//                Assert.assertEquals(userName, retrievedUserName);
//
//                bulkUploadStatusDataService.delete(bulkUploadStatus);
//            }
//        }

    }
    

}
