package org.motechproject.nms.util.it;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.repository.BulkUploadStatusDataService;
import org.motechproject.nms.util.service.BulkUploadStatusService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertNotNull;
/**
 * Verify that HelloWorldRecordService present, functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class BulkUploadStatusServiceIT extends BasePaxIT {

    @Inject
    private BulkUploadStatusService bulkUploadStatusService;

    @Inject
    private BulkUploadStatusDataService bulkUploadStatusDataService;

    @Test
    public void shouldAddBulkUploadStatus() throws Exception {
               assertNotNull(bulkUploadStatusDataService);

        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        bulkUploadStatus.setLogFileServerIp("10.203.3.63");
        BulkUploadError bulkUploadError = new BulkUploadError();
        String logFileName = bulkUploadError.createBulkUploadErrLogFileName("CircleCsv");
        bulkUploadStatus.setLogFileName(logFileName);
        bulkUploadStatus.setBulkUploadFileName("csv-import.someFileName");
        bulkUploadStatus.setNumberOfFailedRecords(82);
        bulkUploadStatus.setNumberOfSuccessfulRecords(3);
        bulkUploadStatusService.add(bulkUploadStatus);

        List<BulkUploadStatus> list = bulkUploadStatusDataService.retrieveAll();
        assertNotNull(list);
        for(BulkUploadStatus status : list) {
            if(status.getLogFileName().equalsIgnoreCase(logFileName)) {
                Assert.assertEquals(bulkUploadStatus.getBulkUploadFileName(), status.getBulkUploadFileName());
                Assert.assertEquals(bulkUploadStatus.getLogFileServerIp(), status.getLogFileServerIp());
                Assert.assertEquals(bulkUploadStatus.getNumberOfFailedRecords(), status.getNumberOfFailedRecords());
                Assert.assertEquals(bulkUploadStatus.getNumberOfSuccessfulRecords(), status.getNumberOfSuccessfulRecords());

                bulkUploadStatusDataService.delete(bulkUploadStatus);
            }
        }

    }
    

}
