package org.motechproject.nms.util.ut;

import org.junit.Test;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created by root on 5/3/15.
 */
public class DataValidationExceptionUT {

    @Test
    public void shouldSetValuesInDataValidationException() {

        String message = "Incorrect Value";
        String field = "Age";
        String description = "Upload unsuccessful as Age is invalid.";
        Throwable cause = new Throwable(new NumberFormatException());
        DataValidationException dataValidationException = new DataValidationException(message, ErrorCategoryConstants.INVALID_DATA, field, cause);
        String expectedMessage = "Incorrect Value for errorCode :Invalid Data";

        assertEquals(expectedMessage, dataValidationException.getMessage());
        assertEquals(ErrorCategoryConstants.INVALID_DATA, dataValidationException.getErrorCode());
        assertEquals(field, dataValidationException.getErroneousField());
        assertEquals(cause, dataValidationException.getCause());

        DataValidationException dataValidationExceptionNew = new DataValidationException(message, ErrorCategoryConstants.INVALID_DATA, field);

        assertEquals(expectedMessage, dataValidationExceptionNew.getMessage());
        assertEquals(ErrorCategoryConstants.INVALID_DATA, dataValidationExceptionNew.getErrorCode());
        assertEquals(field, dataValidationExceptionNew.getErroneousField());

        DataValidationException dataValidationExc =  new DataValidationException(message, ErrorCategoryConstants.INVALID_DATA, String.format(ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION, field), field);
        assertEquals(description, dataValidationExc.getErrorDesc());

        DataValidationException dataValidationEx = new DataValidationException();
        assertNull(dataValidationEx.getErrorCode());
    }
}