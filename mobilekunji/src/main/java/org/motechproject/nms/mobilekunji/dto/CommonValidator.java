package org.motechproject.nms.mobilekunji.dto;

import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * This class contains method to validate contents
 */

public class CommonValidator {

    /**
     *  This method is used to validate Card Number of Two Digits
     *
     *  @param cardNumber
     */
    public static void validateCardNumber(String cardNumber) throws DataValidationException {

        if (cardNumber.length() != ConfigurationConstants.MAX_CARD_DIGITS) {
            ParseDataHelper.raiseInvalidDataException(ConfigurationConstants.CARD_CODE, cardNumber);
        }
    }
}
