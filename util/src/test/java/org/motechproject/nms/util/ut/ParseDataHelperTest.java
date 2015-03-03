package org.motechproject.nms.util.ut;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * Class to test static helper methods for checking empty/null values and
 * parsing the data from string to specific types provided by ParseDataHelper
 */
public class ParseDataHelperTest {

    /**
     * Test case to check that a DataValidationException with error code:INVALID_DATA
     * is raised by method raiseInvalidDataException
     */
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

    /**
     * Test case to check that a DataValidationException with
     * error code:INVALID_DATA and specified cause
     * is raised by method raiseInvalidDataException
     */
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

    /**
     *
     */
    @Test
    public void shouldRaiseMissingDataException() {
        String erroneousField = "Name";
        String erroneousValue = null;

        try {
            ParseDataHelper.raiseMissingDataException(erroneousField, erroneousValue);
        } catch (Exception e) {
            Assert.assertTrue( e instanceof DataValidationException );
            Assert.assertEquals( ((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING );
            Assert.assertEquals( ((DataValidationException) e).getErroneousField(), erroneousField );
        }

        erroneousValue = "";
        try {
            ParseDataHelper.raiseMissingDataException(erroneousField, erroneousValue);
        } catch (Exception e) {
            Assert.assertTrue( e instanceof DataValidationException );
            Assert.assertEquals( ((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING );
            Assert.assertEquals( ((DataValidationException) e).getErroneousField(), erroneousField );
        }

    }

    /**
     *
     */
    @Test
    public void shouldRaiseExceptionForMissingValueOfMandatoryField() {

        String erroneousField = "Name";
        String erroneousValue = null;
        boolean isMandatory = true;
        String returnValue = null;

        try {
            returnValue = ParseDataHelper.parseString(erroneousField, erroneousValue, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue( e instanceof DataValidationException );
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(returnValue);
    }

    /**
     *
     */
    @Test
    public void shouldNotRaiseExceptionForNonNullMandatoryField() {
        String erroneousField = "Name";
        String erroneousValue = "abc";
        boolean isMandatory = true;
        String returnValue = null;

        try {
            returnValue = ParseDataHelper.parseString(erroneousField, erroneousValue, isMandatory);
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(returnValue);
    }

    /**
     *
     */
    @Test
    public void shouldNotRaiseExceptionForMissingNonMandatoryField() {
        String erroneousField = "Name";
        String erroneousValue = null;
        boolean isMandatory = false;
        String returnValue = null;

        try {
            returnValue = ParseDataHelper.parseString(erroneousField, erroneousValue, isMandatory);
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNull(returnValue);
    }

    /**
     *
     */
    @Test
    public void shouldRaiseMissingDataExceptionOnNullValueForMandatoryDateField() {
        String field = "Birth Date";
        String value = null;
        boolean isMandatory = true;
        DateTime dateTimeValue =  null;

        try {
            dateTimeValue = ParseDataHelper.parseDate(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(dateTimeValue);

    }

    /**
     *
     */
    @Test
    public void shouldRaiseInvalidDataExceptionOnInvalidDataForDateField() {
        String field = "Birth Date";
        String value = "1987-19-08 00:00:00";
        boolean isMandatory = true;
        DateTime dateTimeValue = null;

        try {
            dateTimeValue = ParseDataHelper.parseDate(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(dateTimeValue);

        value = "1987-02-58 00:00:00";

        try {
            dateTimeValue = ParseDataHelper.parseDate(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(dateTimeValue);

        value = "1987:02:08 00:00:00";

        try {
            dateTimeValue = ParseDataHelper.parseDate(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(dateTimeValue);

    }

    /**
     *
     */
    @Test
    public void shouldParseDateFromStringToDateTimeFormat() {
        String field = "Birth Date";
        String value = "1987-02-08 00:00:00";
        boolean isMandatory = true;
        DateTime dateTimeValue = null;

        try {
            dateTimeValue = ParseDataHelper.parseDate(field, value, isMandatory);
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(dateTimeValue);
    }

    /**
     *
     */
    @Test
    public void shouldRaiseMissingDataExceptionOnNullValueForMandatoryIntegerField() {
        String field = "languageLocationCode";
        String value = "";
        boolean isMandatory = true;
        Integer intValue = null;

        try {
            intValue = ParseDataHelper.parseInt(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(intValue);

        value = null;

        try {
            intValue = ParseDataHelper.parseInt(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(intValue);

        value = "NULL";

        try {
            intValue = ParseDataHelper.parseInt(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(intValue);
    }

    /**
     *
     */
    @Test
    public void shouldRaiseInvalidDataExceptionOnInvalidDataForIntegerField() {
        String field = "languageLocationCode";
        String value = "1@";
        boolean isMandatory = true;
        Integer intValue = null;

        try {
            intValue = ParseDataHelper.parseInt(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(intValue);

        value = "a";

        try {
            intValue = ParseDataHelper.parseInt(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(intValue);

        value = "1.0";

        try {
            intValue = ParseDataHelper.parseInt(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(intValue);
    }

    /**
     *
     */
    @Test
    public void shouldParseIntegerValueFromString() {
        String field = "languageLocationCode";
        String value = "123";
        boolean isMandatory = true;
        Integer intValue = null;

        try {
            intValue = ParseDataHelper.parseInt(field, value, isMandatory);
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(intValue);
    }

    /**
     *
     */
    @Test
    public void shouldRaiseMissingDataExceptionOnNullValueForMandatoryLongField() {
        String field = "stateCode";
        String value = "";
        boolean isMandatory = true;
        Long longValue = null;

        try {
            longValue = ParseDataHelper.parseLong(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(longValue);

        value = null;

        try {
            longValue = ParseDataHelper.parseLong(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(longValue);

        value = "NULL";

        try {
            longValue = ParseDataHelper.parseLong(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(longValue);
    }

    /**
     *
     */
    @Test
    public void shouldRaiseInvalidDataExceptionOnInvalidDataForLongField() {
        String field = "districtCode";
        String value = "1@";
        boolean isMandatory = true;
        Long longValue = null;

        try {
            longValue = ParseDataHelper.parseLong(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(longValue);

        value = "a";

        try {
            longValue = ParseDataHelper.parseLong(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(longValue);

        value = "1.0";

        try {
            longValue = ParseDataHelper.parseLong(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(longValue);
    }

    /**
     *
     */
    @Test
    public void shouldParseLongValueFromString() {
        String field = "stateCode";
        String value = "1234567";
        boolean isMandatory = true;
        Long longValue = null;
        Long expectedValue = 1234567L;

        try {
            longValue = ParseDataHelper.parseLong(field, value, isMandatory);
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(longValue);
        Assert.assertEquals(expectedValue, longValue);
    }

    /**
     *
     */
    @Test
    public void shouldRaiseMissingDataExceptionOnNullValueForMandatoryBooleanField() {
        String field = "stateCode";
        String value = "";
        boolean isMandatory = true;
        Boolean boolValue = null;

        try {
            boolValue = ParseDataHelper.parseBoolean(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(boolValue);

        value = null;

        try {
            boolValue = ParseDataHelper.parseBoolean(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(boolValue);

        value = "NULL";

        try {
            boolValue = ParseDataHelper.parseBoolean(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(boolValue);
    }

    /**
     *
     */
    @Test
    public void shouldRaiseInvalidDataExceptionOnInvalidDataForBooleanField() {
        String field = "isDeactivatedBySystem";
        String value = "1";
        boolean isMandatory = true;
        Boolean boolValue = null;

        try {
            boolValue = ParseDataHelper.parseBoolean(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(boolValue);

        value = "@";

        try {
            boolValue = ParseDataHelper.parseBoolean(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(boolValue);

        value = "0";

        try {
            boolValue = ParseDataHelper.parseBoolean(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(boolValue);
    }

    /**
     *
     */
    @Test
    public void shouldParseBooleanValueFromString() {
        String field = "isDeactivatedBySystem";
        String value = "TRuE";
        boolean isMandatory = true;
        Boolean boolValue = null;

        try {
            boolValue = ParseDataHelper.parseBoolean(field, value, isMandatory);
        } catch (Exception e) {
            Assert.fail();
        }

        Assert.assertNotNull(boolValue);
        Assert.assertEquals(true, boolValue);
    }
}
