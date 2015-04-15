package org.motechproject.nms.mobilekunji.dto;

import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * This class contains method to validate contents
 */

public class CommonValidator {

    public static void validateCardNumber(String cardNumber) throws DataValidationException {

        if (cardNumber.length() != ConfigurationConstants.MAX_CARD_DIGITS) {
            ParseDataHelper.raiseInvalidDataException(ConfigurationConstants.CARD_NUMBER, cardNumber);
        }
    }
}
