package org.motechproject.nms.util.ut;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.helper.NmsUtils;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Test class to test BulkUploadStatus
 */
public class BulkUploadStatusTest {

    /**
     * Test class to test that the fields are
     * correctly being set in the BulkUploadStatus object.
     */
     @Test
     public void shouldSetValuesInFieldsOfBulkUploadStatus() {
         String uploadedBy = "user-name";
         String bulkUploadFileName = "csv-import.fileName";
         Integer numberOfFailedRecords = 82;
         Integer numberOfSuccessfulRecords = 3;
         DateTime timeOfUpload = NmsUtils.getCurrentTimeStamp();

         BulkUploadStatus bulkUploadStatus = new BulkUploadStatus(uploadedBy, bulkUploadFileName, timeOfUpload, numberOfFailedRecords, numberOfSuccessfulRecords);

         assertEquals(bulkUploadFileName, bulkUploadStatus.getBulkUploadFileName());
         assertEquals(numberOfFailedRecords, bulkUploadStatus.getNumberOfFailedRecords());
         assertEquals(numberOfSuccessfulRecords, bulkUploadStatus.getNumberOfSuccessfulRecords());
         assertEquals(uploadedBy, bulkUploadStatus.getUploadedBy());
         assertEquals(timeOfUpload, bulkUploadStatus.getTimeOfUpload());

         BulkUploadStatus bulkUploadStatusNew = new BulkUploadStatus();
         assertTrue(0 == bulkUploadStatusNew.getNumberOfSuccessfulRecords());
         assertTrue(0 == bulkUploadStatusNew.getNumberOfFailedRecords());

         numberOfFailedRecords = 4;
         numberOfSuccessfulRecords = 56;
         bulkUploadFileName = "csv-import.fileNameNew";

         bulkUploadStatusNew.setNumberOfFailedRecords(numberOfFailedRecords);
         bulkUploadStatusNew.setNumberOfSuccessfulRecords(numberOfSuccessfulRecords);
         bulkUploadStatusNew.setTimeOfUpload(timeOfUpload);
         bulkUploadStatusNew.setUploadedBy(uploadedBy);
         bulkUploadStatusNew.setBulkUploadFileName(bulkUploadFileName);

         assertEquals(bulkUploadFileName, bulkUploadStatusNew.getBulkUploadFileName());
         assertEquals(numberOfFailedRecords, bulkUploadStatusNew.getNumberOfFailedRecords());
         assertEquals(numberOfSuccessfulRecords, bulkUploadStatusNew.getNumberOfSuccessfulRecords());
         assertEquals(uploadedBy, bulkUploadStatusNew.getUploadedBy());
         assertEquals(timeOfUpload, bulkUploadStatusNew.getTimeOfUpload());


     }

}
