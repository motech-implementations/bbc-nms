package org.motechproject.nms.util.ut;

import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;

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
    }
}
