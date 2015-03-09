package org.motechproject.nms.util.ut;

import org.junit.Test;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;

import static junit.framework.Assert.assertEquals;


/**
 * Created by root on 4/3/15.
 */
public class BulkUploadStatusTest {

        @Test
        public void shouldSetValuesInFieldsOfBulkUploadStatus() {

            BulkUploadError bulkUploadError = new BulkUploadError();
            String logFileName = bulkUploadError.createBulkUploadErrLogFileName("CircleCsv");
            String bulkUploadFileName = "csv-import.someFileName";
            Integer numberOfFailedRecords = 82;
            Integer numberOfSuccessfulRecords = 3;
            String logFileServerIP = "10.203.3.63";

            BulkUploadStatus bulkUploadStatus = new BulkUploadStatus(bulkUploadFileName, numberOfFailedRecords, numberOfSuccessfulRecords, logFileName, logFileServerIP);
            assertEquals(logFileName, bulkUploadStatus.getLogFileName());
            assertEquals(bulkUploadFileName, bulkUploadStatus.getBulkUploadFileName());
            assertEquals(logFileServerIP, bulkUploadStatus.getLogFileServerIp());
            assertEquals(numberOfFailedRecords, bulkUploadStatus.getNumberOfFailedRecords());
            assertEquals(numberOfSuccessfulRecords, bulkUploadStatus.getNumberOfSuccessfulRecords());
        }

}
