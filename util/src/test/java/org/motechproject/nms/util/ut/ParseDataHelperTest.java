package org.motechproject.nms.util.ut;

import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * Created by root on 2/3/15.
 */
public class ParseDataHelperTest {

    @Test
    public void shouldRaiseInvalidDataException() {
        String erroneousField = "Name";
        String erroneousValue = "abcdef";
        try {
            ParseDataHelper.raiseInvalidDataException(erroneousField, "Name");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
            Assert.assertEquals(((DataValidationException)e).getErroneousField(), erroneousField);
        }
    }

    @Test
    public void shouldRaiseInvalidDataExceptionForGivenException() {

            String erroneousField = "Name";
            String erroneousValue = "1234";
            NumberFormatException cause = new NumberFormatException();
            try {
                ParseDataHelper.raiseInvalidDataException(erroneousField, "Name", new NumberFormatException());
            } catch (Exception e) {
                Assert.assertTrue(e instanceof DataValidationException);
                Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
                Assert.assertEquals(((DataValidationException) e).getErroneousField(), erroneousField);
            }

    }

    @Test
    public void shouldRaiseMissingDataException() {
        String erroneousField = "Name";
        String erroneousValue = null;
        NumberFormatException cause = new NumberFormatException();
        try {
            ParseDataHelper.raiseMissingDataException(erroneousField, erroneousValue);
        } catch (Exception e) {
            Assert.assertTrue( e instanceof DataValidationException );
            Assert.assertEquals( ((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING );
            Assert.assertEquals( ((DataValidationException) e).getErroneousField(), erroneousField );
        }

    }

    @Test
    public void shouldThrowExceptionForMissingValueOfMandatoryField() {

        String erroneousField = "Name";
        String erroneousValue = null;
        boolean isMandatory = true;

        try {
            ParseDataHelper.parseString(erroneousField, erroneousValue, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue( e instanceof DataValidationException );
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }
    }

    @Test
    public void shouldNotThrowExceptionForNonNullMandatoryField() {
        String erroneousField = "Name";
        String erroneousValue = "abc";
        boolean isMandatory = true;

        try {
            ParseDataHelper.parseString(erroneousField, erroneousValue, isMandatory);
        } catch (Exception e) {
            Assert.fail();
        }

    }

    @Test
    public void shouldNotThrowExceptionForMissingNonMandatoryField() {
        String erroneousField = "Name";
        String erroneousValue = null;
        boolean isMandatory = false;

        try {
            ParseDataHelper.parseString(erroneousField, erroneousValue, isMandatory);
        } catch (Exception e) {
            Assert.fail();
        }
    }

}
