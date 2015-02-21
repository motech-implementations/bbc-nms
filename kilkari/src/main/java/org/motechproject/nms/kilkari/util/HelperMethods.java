package org.motechproject.nms.kilkari.util;

import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;

public class HelperMethods {

    public static void raiseInvalidException(String fieldName, String fieldValue) throws DataValidationException {
        
        String errMessage = String.format(DataValidationException.INVALID_FORMAT_MESSAGE, fieldValue);
        throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, fieldName);
    }
}
