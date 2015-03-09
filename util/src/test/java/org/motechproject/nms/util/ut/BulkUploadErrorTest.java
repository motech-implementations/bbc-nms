package org.motechproject.nms.util.ut;

import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;

/**
 * Created by root on 9/3/15.
 */
public class BulkUploadErrorTest {

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
