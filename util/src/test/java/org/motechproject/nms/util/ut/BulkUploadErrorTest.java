package org.motechproject.nms.util.ut;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.RecordType;
import org.motechproject.nms.util.helper.NmsUtils;

/**
 * Test class to test the BulkUploadError.
 */
public class BulkUploadErrorTest {

    /**
     * Test case to test that error category, description and record details
     * are correctly being set in the BulkUploadError object.
     */
    @Test
    public void shouldSetValuesInBulkUploadError() {

        BulkUploadError bulkUploadError = new BulkUploadError();

        bulkUploadError.setErrorCategory(ErrorCategoryConstants.GENERAL_EXCEPTION);
        Assert.assertEquals(ErrorCategoryConstants.GENERAL_EXCEPTION, bulkUploadError.getErrorCategory());

        bulkUploadError.setErrorDescription(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION);
        Assert.assertEquals(ErrorDescriptionConstants.GENERAL_EXCEPTION_DESCRIPTION, bulkUploadError.getErrorDescription());

        bulkUploadError.setRecordDetails("Record Detail");
        Assert.assertEquals("Record Detail", bulkUploadError.getRecordDetails());

        bulkUploadError.setRecordType(RecordType.CIRCLE);
        Assert.assertEquals(RecordType.CIRCLE, bulkUploadError.getRecordType());

        bulkUploadError.setCsvName("Name.csv");
        Assert.assertEquals("Name.csv", bulkUploadError.getCsvName());

        DateTime time = NmsUtils.getCurrentTimeStamp();
        bulkUploadError.setTimeOfUpload(time);
        Assert.assertEquals(time, bulkUploadError.getTimeOfUpload());
    }
}
