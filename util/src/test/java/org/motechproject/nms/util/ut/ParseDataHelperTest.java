package org.motechproject.nms.util.ut;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.util.constants.Constants;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * Class to test static helper methods for checking empty/null values and
 * parsing the data from string to specific types provided by ParseDataHelper
 */
public class ParseDataHelperTest {

    /**
     * Test case to check that a DataValidationException
     * with error code:INVALID_DATA
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
     * Test case to check that a DataValidationException
     * with error code:MANDATORY_PARAMETER_MISSING
     * is raised by method raiseMissingDataException
     */
    @Test
    public void shouldRaiseMissingDataException() {
        String erroneousField = "Name";

        //Testing with null
        String erroneousValue = null;

        try {
            ParseDataHelper.raiseMissingDataException(erroneousField, erroneousValue);
        } catch (Exception e) {
            Assert.assertTrue( e instanceof DataValidationException );
            Assert.assertEquals( ((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING );
            Assert.assertEquals( ((DataValidationException) e).getErroneousField(), erroneousField );
        }

        //Testing with empty string
        erroneousValue = Constants.EMPTY_STRING;
        try {
            ParseDataHelper.raiseMissingDataException(erroneousField, erroneousValue);
        } catch (Exception e) {
            Assert.assertTrue( e instanceof DataValidationException );
            Assert.assertEquals( ((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING );
            Assert.assertEquals( ((DataValidationException) e).getErroneousField(), erroneousField );
        }

    }

    /**
     * Test case to test parseString method for null value
     * of a mandatory field
     */
    @Test
    public void shouldRaiseExceptionForNullInMandatoryStringField() {

        String erroneousField = "Name";
        boolean isMandatory = true;
        String returnValue = null;

        //testing with null
        String erroneousValue = null;

        try {
            returnValue = ParseDataHelper.parseString(erroneousField, erroneousValue, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue( e instanceof DataValidationException );
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(returnValue);

        //testing with an empty string
        erroneousValue = Constants.EMPTY_STRING;

        try {
            returnValue = ParseDataHelper.parseString(erroneousField, erroneousValue, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue( e instanceof DataValidationException );
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(returnValue);

    }

    /**
     * Test case to test parseString method for a valid value
     * of a mandatory field. String should be successfully parsed.
     */
    @Test
    public void shouldNotRaiseExceptionForNonNullMandatoryStringField() {
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
     * Test case to test parseString method with null value
     * for a non mandatory field.
     * No exception is expected and method should return null.
     */
    @Test
    public void shouldNotRaiseExceptionForMissingNonMandatoryStringField() {
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
     * Test case to test parseDate method with null value for mandatory field.
     * DataValidationException with error code : MANDATORY_PARAMETER_MISSING is expected
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
     * Test case to test parseDate method with invalid date.
     * DataValidationException with error code : INVALID_DATA is expected
     */
    @Test
    public void shouldRaiseInvalidDataExceptionOnInvalidDataForDateField() {
        String field = "Birth Date";
        boolean isMandatory = true;
        DateTime dateTimeValue = null;

        // Invalid input 1 (Invalid month)
        String value = "1987-19-08 00:00:00";

        try {
            dateTimeValue = ParseDataHelper.parseDate(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(dateTimeValue);

        // Invalid input 2 (Invalid date)
        value = "1987-02-58 00:00:00";

        try {
            dateTimeValue = ParseDataHelper.parseDate(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(dateTimeValue);

        // Invalid input 3 (Invalid format)
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
     * Test case to test parseDate method with a valid date in String format.
     * String should be successfully parsed to a date in simple date format.
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
     * Test case to test parseInt method with null/empty string for mandatory field.
     * DataValidationException with error code : MANDATORY_PARAMETER_MISSING is expected
     */
    @Test
    public void shouldRaiseMissingDataExceptionOnNullValueForMandatoryIntegerField() {
        String field = "languageLocationCode";
        String value = null;
        boolean isMandatory = true;
        Integer intValue = null;

        //Testing with null
        try {
            intValue = ParseDataHelper.parseInt(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(intValue);

        //Tesing with empty string
        value = Constants.EMPTY_STRING;

        try {
            intValue = ParseDataHelper.parseInt(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(intValue);
    }

    /**
     * Test case to test parseInt method with invalid values.
     * DataValidationException with error code : INVALID_DATA is expected
     */
    @Test
    public void shouldRaiseInvalidDataExceptionOnInvalidDataForIntegerField() {
        String field = "languageLocationCode";
        boolean isMandatory = true;
        Integer intValue = null;

        // Invalid input 1
        String value = "1@";

        try {
            intValue = ParseDataHelper.parseInt(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(intValue);

        // Invalid input 2
        value = "a";

        try {
            intValue = ParseDataHelper.parseInt(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(intValue);

        // Invalid input 3
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
     * Test case to test parseInt method with a valid int value as String.
     * String should be successfully parsed to integer value.
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
     * Test case to test parseLong method with null/empty string for mandatory field.
     * DataValidationException with error code : MANDATORY_PARAMETER_MISSING is expected
     */
    @Test
    public void shouldRaiseMissingDataExceptionOnNullValueForMandatoryLongField() {
        String field = "stateCode";
        boolean isMandatory = true;
        Long longValue = null;

        //Testing with null
        String value = null;

        try {
            longValue = ParseDataHelper.parseLong(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(longValue);

        //Testing with empty string
        value = Constants.EMPTY_STRING;

        try {
            longValue = ParseDataHelper.parseLong(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(longValue);
    }

    /**
     * Test case to test parseLong method with invalid values.
     * DataValidationException with error code : INVALID_DATA is expected
     */
    @Test
    public void shouldRaiseInvalidDataExceptionOnInvalidDataForLongField() {
        String field = "districtCode";
        boolean isMandatory = true;
        Long longValue = null;

        //Invalid input 1
        String value = "1@";
        try {
            longValue = ParseDataHelper.parseLong(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(longValue);

        //Invalid input 2
        value = "a";

        try {
            longValue = ParseDataHelper.parseLong(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(longValue);

        //Invalid input 3
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
     * Test case to test parseLong method with a valid long value as String.
     * String should be successfully parsed to long value.
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
     * Test case to test parseBoolean method with null/empty input for mandatory field.
     * DataValidationException with error code : MANDATORY_PARAMETER_MISSING is expected
     */
    @Test
    public void shouldRaiseMissingDataExceptionOnNullValueForMandatoryBooleanField() {
        String field = "stateCode";
        boolean isMandatory = true;
        Boolean boolValue = null;

        //Testing with null
        String value = null;
        try {
            boolValue = ParseDataHelper.parseBoolean(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(boolValue);

        //Testing with empty string
        value = Constants.EMPTY_STRING;

        try {
            boolValue = ParseDataHelper.parseBoolean(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertNull(boolValue);
    }

    /**
     * Test case to test parseBoolean method with invalid values.
     * DataValidationException with error code : INVALID_DATA is expected
     */
    @Test
    public void shouldRaiseInvalidDataExceptionOnInvalidDataForBooleanField() {
        String field = "isDeactivatedBySystem";
        boolean isMandatory = true;
        Boolean boolValue = null;

        //Invalid input 1
        String value = "1";

        try {
            boolValue = ParseDataHelper.parseBoolean(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(boolValue);

        //Invalid input 2
        value = "@";

        try {
            boolValue = ParseDataHelper.parseBoolean(field, value, isMandatory);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException)e).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
        }

        Assert.assertNull(boolValue);

        //Invalid input 3
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
     * Test case to test parseBoolean method with
     * a valid Boolean value(true or false) as String.
     * String should be successfully parsed to Boolean
     */
    @Test
    public void shouldParseBooleanValueFromString() {
        String field = "isDeactivatedBySystem";
        String value = "TRuE"; //This value should not be case sensitive
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
