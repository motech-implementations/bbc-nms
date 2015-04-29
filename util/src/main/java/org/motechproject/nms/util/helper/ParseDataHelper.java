package org.motechproject.nms.util.helper;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.nms.util.constants.Constants;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class provides static helper methods for checking empty/null values and
 * parsing the data from string to specific types. Helper methods also raises
 * exception if parameters is invalid or empty/null (if mandatory).
 */
public final class ParseDataHelper {

    private static Logger logger = LoggerFactory
            .getLogger(ParseDataHelper.class);

    private ParseDataHelper() {

    }

    /**
     * checks if the field value is null or empty or blank
     *
     * @param field The value of the field to be checked
     * @return true if null or empty or blank else false
     */
    public static boolean isNullOrEmpty(String field) {
        // "NULL" with ignore case is also considered as empty
        return (StringUtils.isBlank(field) || StringUtils.equalsIgnoreCase(
                field, "NULL"));
    }

    /**
     * This method validates a field of String type for null/empty values, and
     * raises exception if a mandatory field is empty/null
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value fo the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field
     *         value is returned
     * @throws DataValidationException
     */
    public static String validateAndParseString(String fieldName,
            String fieldValue, boolean isMandatory)
            throws DataValidationException {

        if (isNullOrEmpty(fieldValue)) {
            if (isMandatory) {
                raiseMissingDataException(fieldName, fieldValue);
            } else {
                return null;
            }
        }
        return fieldValue;
    }

    /**
     * This method validates a field of String type for null/empty values, and
     * raises exception if a mandatory field is empty/null. The String "null" is
     * considered as a valid string
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value of the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and it is null or its value is empty,
     *         else field value is returned
     * @throws DataValidationException in case of null or empty mandatory
     *             parameter
     */
    public static String validateString(String fieldName, String fieldValue,
            boolean isMandatory) throws DataValidationException {
        if (isMandatory) {
            if (fieldValue == null) {
                raiseApiParameterMissingDataException(fieldName, fieldValue);
            } else if (StringUtils.isBlank(fieldValue)) {
                raiseApiParameterInvalidDataException(fieldName, fieldValue);
            }
        }
        return fieldValue;
    }

    /**
     * This method validates a field of Date type for null/empty values, and
     * raises exception if a mandatory field is empty/null or is invalid date
     * format
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value fo the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field
     *         value is converted from string to DateTime and returned
     * @throws DataValidationException
     */
    public static DateTime validateAndParseDate(String fieldName,
            String fieldValue, boolean isMandatory)
            throws DataValidationException {
        DateTime parsedDateTime = null;

        try {
            if (validateAndParseString(fieldName, fieldValue, isMandatory) != null) {
                DateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                dateFormat.setLenient(false);
                Date date = dateFormat.parse(fieldValue);
                parsedDateTime = new DateTime(date);
            }

        } catch (NumberFormatException | ParseException e) {
            raiseInvalidDataException(fieldName, fieldValue, e);
        }

        return parsedDateTime;
    }

    /**
     * This method validates a field of Date type for null/empty/invalid values,
     * and raises exception if a mandatory field is empty/null or is invalid
     * date format
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value of the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field
     *         value is converted from string to DateTime and returned
     * @throws DataValidationException
     */
    public static DateTime validateDate(String fieldName, String fieldValue,
            boolean isMandatory) throws DataValidationException {

        DateTime parsedDateTime = null;

        try {
            if (validateString(fieldName, fieldValue, isMandatory) != null) {
                DateFormat dateFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                dateFormat.setLenient(false);
                Date date = dateFormat.parse(fieldValue);
                parsedDateTime = new DateTime(date);
            }
        } catch (NumberFormatException | ParseException e) {
            raiseApiParameterInvalidDataException(fieldName, fieldValue);
        }

        return parsedDateTime;
    }

    /**
     * This method validates a field of Integer type for null/empty values, and
     * raises exception if a mandatory field is empty/null or is invalid Integer
     * format
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value fo the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field
     *         value is converted from string to Integer and returned
     * @throws DataValidationException
     */
    public static Integer validateAndParseInt(String fieldName,
            String fieldValue, boolean isMandatory)
            throws DataValidationException {
        Integer parsedValue = null;
        try {
            if (validateAndParseString(fieldName, fieldValue, isMandatory) != null) {
                parsedValue = Integer.parseInt(fieldValue);
            }
        } catch (NumberFormatException e) {
            raiseInvalidDataException(fieldName, fieldValue, e);
        }

        return parsedValue;
    }

    /**
     * This method validates a field of Integer type for null/empty values, and
     * raises exception if a mandatory field is empty/null or is invalid Integer
     * format
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value of the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field
     *         value is converted from string to Integer and returned
     * @throws DataValidationException
     */
    public static Integer validateInt(String fieldName, String fieldValue,
            boolean isMandatory) throws DataValidationException {
        Integer parsedValue = null;
        try {
            if (validateString(fieldName, fieldValue, isMandatory) != null) {
                parsedValue = Integer.parseInt(fieldValue);
            }
        } catch (NumberFormatException e) {
            raiseApiParameterInvalidDataException(fieldName, fieldValue);
        }

        return parsedValue;
    }

    /**
     * This method validates a field of Long type for null/empty values, and
     * raises exception if a mandatory field is empty/null or is invalid Long
     * format
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value fo the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field
     *         value is converted from string to Long and returned
     * @throws DataValidationException
     */
    public static Long validateAndParseLong(String fieldName,
            String fieldValue, boolean isMandatory)
            throws DataValidationException {
        Long parsedValue = null;
        try {
            if (validateAndParseString(fieldName, fieldValue, isMandatory) != null) {
                parsedValue = Long.parseLong(fieldValue);
            }
        } catch (NumberFormatException e) {
            raiseInvalidDataException(fieldName, fieldValue, e);
        }

        return parsedValue;
    }

    /**
     * This method validates a field of Long type for null/empty values, and
     * raises exception if a mandatory field is empty/null or is invalid Long
     * format
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value of the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field
     *         value is converted from string to Long and returned
     * @throws DataValidationException
     */
    public static Long validateLong(String fieldName, String fieldValue,
            boolean isMandatory) throws DataValidationException {
        Long parsedValue = null;
        try {
            if (validateString(fieldName, fieldValue, isMandatory) != null) {
                parsedValue = Long.parseLong(fieldValue);
            }
        } catch (NumberFormatException e) {
            raiseApiParameterInvalidDataException(fieldName, fieldValue);
        }

        return parsedValue;
    }

    /**
     * This method validates a field of Long type for null values, and raises
     * exception if a field is null.
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value of the field
     * @return field value
     * @throws DataValidationException
     */
    public static Long validateLong(String fieldName, Long fieldValue)
            throws DataValidationException {

        if (fieldValue == null) {
            raiseApiParameterMissingDataException(fieldName, null);
        }
        return fieldValue;
    }

    /**
     * This method validates a field of Boolean type for null values, and raises
     * exception if a field is null
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value of the field
     * @return field value
     * @throws DataValidationException
     */
    public static Boolean validateBoolean(String fieldName, Boolean fieldValue)
            throws DataValidationException {

        if (fieldValue == null) {
            raiseApiParameterMissingDataException(fieldName, null);
        }
        return fieldValue;
    }

    /**
     * This method validates a Integer type for null values, and raises
     * exception if field is null
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value of the field
     * @return field value
     * @throws DataValidationException
     */
    public static Integer validateInt(String fieldName, Integer fieldValue)
            throws DataValidationException {

        if (fieldValue == null) {
            raiseApiParameterMissingDataException(fieldName, null);
        }

        return fieldValue;
    }

    /**
     * This method validates a field of Boolean type for null/empty values, and
     * raises exception if a mandatory field is empty/null or is invalid format
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value fo the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field
     *         value is converted from string to Boolean and returned
     * @throws DataValidationException
     */
    public static Boolean validateAndParseBoolean(String fieldName,
            String fieldValue, boolean isMandatory)
            throws DataValidationException {
        Boolean parsedValue = null;
        if (validateAndParseString(fieldName, fieldValue, isMandatory) != null) {
            if (fieldValue.equalsIgnoreCase("true")
                    || fieldValue.equalsIgnoreCase(("false"))) {
                parsedValue = Boolean.parseBoolean(fieldValue);
            } else {
                raiseInvalidDataException(fieldName, fieldValue);
            }
        }

        return parsedValue;
    }

    /**
     * This method validates a field of Boolean type for null/empty values, and
     * raises exception if a mandatory field is empty/null or is invalid format
     *
     * @param fieldName name of the field to be used in exception
     * @param fieldValue value of the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field
     *         value is converted from string to Boolean and returned
     * @throws DataValidationException
     */
    public static Boolean validateBoolean(String fieldName, String fieldValue,
            boolean isMandatory) throws DataValidationException {
        Boolean parsedValue = null;
        if (validateString(fieldName, fieldValue, isMandatory) != null) {
            if (fieldValue.equalsIgnoreCase("true")
                    || fieldValue.equalsIgnoreCase(("false"))) {
                parsedValue = Boolean.parseBoolean(fieldValue);
            } else {
                raiseApiParameterInvalidDataException(fieldName, fieldValue);
            }
        }

        return parsedValue;
    }

    /**
     * This methods validates that Msisdn is of 10 or more digits and then trims
     * it to 10 digits
     *
     * @param fieldName Name of the field
     * @param msisdn value of the field
     * @return 10 digit msisdn value
     * @throws DataValidationException
     */
    public static String validateAndTrimMsisdn(String fieldName, String msisdn)
            throws DataValidationException {

        int msisdnLength = msisdn.length();

        if (!StringUtils.isNumeric(msisdn)
                || msisdnLength < Constants.MSISDN_LENGTH) {
            ParseDataHelper.raiseInvalidDataException(fieldName, msisdn);
        } else if (msisdnLength > Constants.MSISDN_LENGTH) {
            msisdn = msisdn.substring(msisdnLength - Constants.MSISDN_LENGTH,
                    msisdnLength);
        }
        return msisdn;
    }

    /**
     * This method validates CallId for length and being numeric and throws
     * DataValidationException with error cause INVALID_DATA in case of
     * validation failure.
     *
     * @param fieldName Name of the field
     * @param callId value of callId
     * @return true if validation is successful else false
     * @throws DataValidationException
     */
    public static Boolean validateLengthOfCallId(String fieldName, String callId)
            throws DataValidationException {

        if (null != callId) {

            if (StringUtils.isNumeric(callId.trim())
                    && callId.trim().length() <= Constants.CALL_ID_LENGTH) {
                return true;
            } else {
                ParseDataHelper.raiseApiParameterInvalidDataException(
                        fieldName, callId);
            }
        }
        return false;
    }

    /**
     * Raises exception for invalid data
     * 
     * @param fieldName Name of the field
     * @param fieldValue value of the field
     * @param e Exception raised due to invalid format
     * @throws DataValidationException
     */
    public static void raiseInvalidDataException(String fieldName,
            String fieldValue, Exception e) throws DataValidationException {
        String errMessage = String.format(
                DataValidationException.INVALID_FORMAT_MESSAGE, fieldName,
                fieldValue);
        String errDesc = String.format(
                ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION, fieldName);
        logger.error(errMessage);
        throw new DataValidationException(errMessage,
                ErrorCategoryConstants.INVALID_DATA, errDesc, fieldName, e);
    }

    /**
     * Raises exception for invalid data
     * 
     * @param fieldName Name of the field
     * @param fieldValue value of the field
     * @throws DataValidationException
     */
    public static void raiseInvalidDataException(String fieldName,
            String fieldValue) throws DataValidationException {
        String errMessage = String.format(
                DataValidationException.INVALID_FORMAT_MESSAGE, fieldName,
                fieldValue);
        String errDesc = String.format(
                ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION, fieldName);
        logger.error(errMessage);
        throw new DataValidationException(errMessage,
                ErrorCategoryConstants.INVALID_DATA, errDesc, fieldName);
    }

    /**
     * Raises exception for Missing data
     * 
     * @param fieldName Name of the field
     * @param fieldValue value of the field
     * @throws DataValidationException
     */
    public static void raiseMissingDataException(String fieldName,
            String fieldValue) throws DataValidationException {
        String errMessage = String.format(
                DataValidationException.MANDATORY_MISSING_MESSAGE, fieldName,
                fieldValue);
        String errDesc = String
                .format(ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION,
                        fieldName);
        logger.error(errMessage);
        throw new DataValidationException(errMessage,
                ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING, errDesc,
                fieldName);
    }

    /**
     * Raises exception for invalid data in API
     *
     * @param fieldName Name of the field
     * @param fieldValue value of the field
     * @throws DataValidationException
     */
    public static void raiseApiParameterInvalidDataException(String fieldName,
            String fieldValue) throws DataValidationException {
        String errMessage = String.format(
                DataValidationException.INVALID_FORMAT_MESSAGE, fieldName,
                fieldValue);
        String errDesc = String.format(
                ErrorDescriptionConstants.INVALID_API_PARAMETER_DESCRIPTION,
                fieldName);
        logger.error(errMessage);
        throw new DataValidationException(errMessage,
                ErrorCategoryConstants.INVALID_DATA, errDesc, fieldName);
    }

    /**
     * Raises exception for Missing data in API
     *
     * @param fieldName Name of the field
     * @param fieldValue value of the field
     * @throws DataValidationException
     */
    public static void raiseApiParameterMissingDataException(String fieldName,
            String fieldValue) throws DataValidationException {
        String errMessage = String.format(
                DataValidationException.MANDATORY_MISSING_MESSAGE, fieldName,
                fieldValue);
        String errDesc = String.format(
                ErrorDescriptionConstants.MISSING_API_PARAMETER_DESCRIPTION,
                fieldName);
        logger.error(errMessage);
        throw new DataValidationException(errMessage,
                ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING, errDesc,
                fieldName);
    }
}
