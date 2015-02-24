package org.motechproject.nms.util.helper;

import org.joda.time.DateTime;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * This class provides static helper methods for checking empty/null values and
 * parsing the data from string to specific types.
 * Helper methods also raises exception if parameters is invalid or empty/null (if mandatory).
 */
public final class ParseDataHelper {

    private ParseDataHelper() {

    }

    /**
     * checks if the field value is null or empty
     *
     * @param field The value of the field to be checked
     * @return true if null or empty else false
     */
    public static boolean isNullOrEmpty(String field) {
            /* "NULL" with ignore case is also considered as empty */
        return (field == null || "".equals(field.trim()) || "NULL".equalsIgnoreCase(field.trim()));
    }

    /**
     * This method validates a field of String type for null/empty values, and raises exception if a
     * mandatory field is empty/null
     *
     * @param fieldName   name of the field to be used in exception
     * @param fieldValue  value fo the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field value is returned
     * @throws DataValidationException
     */
    public static String parseString(String fieldName, String fieldValue, boolean isMandatory)
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
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid date format
     *
     * @param fieldName   name of the field to be used in exception
     * @param fieldValue  value fo the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field value is converted
     * from string to DateTime and returned
     * @throws DataValidationException
     */
    public static DateTime parseDate(String fieldName, String fieldValue, boolean isMandatory)
            throws DataValidationException {
        DateTime parsedDateTime = null;

        try {
            if (parseString(fieldName, fieldValue, isMandatory) != null) {
                DateFormat dateFormat = new SimpleDateFormat("");

                Date date = dateFormat.parse(fieldValue);
                parsedDateTime = new DateTime(date);

            }

        } catch (NumberFormatException | ParseException e) {
            raiseInvalidDataException(fieldName, fieldValue, e);
        }

        return parsedDateTime;
    }

    /**
     * This method validates a field of Integer type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid Integer format
     *
     * @param fieldName   name of the field to be used in exception
     * @param fieldValue  value fo the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field value is converted
     * from string to Integer and returned
     * @throws DataValidationException
     */
    public static Integer parseInt(String fieldName, String fieldValue, boolean isMandatory)
            throws DataValidationException {
        Integer parsedValue = null;
        try {
            if (parseString(fieldName, fieldValue, isMandatory) != null) {
                parsedValue = Integer.parseInt(fieldValue);
            }
        } catch (NumberFormatException e) {
            raiseInvalidDataException(fieldName, fieldValue, e);
        }

        return parsedValue;
    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid Long format
     *
     * @param fieldName   name of the field to be used in exception
     * @param fieldValue  value fo the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field value is converted
     * from string to Long and returned
     * @throws DataValidationException
     */
    public static Long parseLong(String fieldName, String fieldValue, boolean isMandatory)
            throws DataValidationException {
        Long parsedValue = null;
        try {
            if (parseString(fieldName, fieldValue, isMandatory) != null) {
                parsedValue = Long.parseLong(fieldValue);
            }
        } catch (NumberFormatException e) {
            raiseInvalidDataException(fieldName, fieldValue, e);
        }

        return parsedValue;
    }

    /**
     * This method validates a field of Date type for null/empty values, and raises exception if a
     * mandatory field is empty/null or is invalid Boolean format
     *
     * @param fieldName   name of the field to be used in exception
     * @param fieldValue  value fo the field
     * @param isMandatory true if field is mandatory, else false
     * @return null if field is optional and its value is null/empty, else field value is converted
     * from string to Boolean and returned
     * @throws DataValidationException
     */
    public static Boolean parseBoolean(String fieldName, String fieldValue, boolean isMandatory)
            throws DataValidationException {
        Boolean parsedValue = null;
        try {
            if (parseString(fieldName, fieldValue, isMandatory) != null) {
                parsedValue = Boolean.parseBoolean(fieldValue);
            }
        } catch (NumberFormatException e) {
            raiseInvalidDataException(fieldName, fieldValue, e);
        }

        return parsedValue;
    }

    public static void raiseInvalidDataException(String fieldName, String fieldValue, Exception e)
            throws DataValidationException {
        String errMessage = DataValidationException.INVALID_FORMAT_MESSAGE.format(fieldValue);
        String errDesc = String.format(ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION, fieldName);
        throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, errDesc, fieldName, e);
    }


    public static void raiseInvalidDataException(String fieldName, String fieldValue)
            throws DataValidationException {
        String errMessage = DataValidationException.INVALID_FORMAT_MESSAGE.format(fieldValue);
        String errDesc = String.format(ErrorDescriptionConstants.INVALID_DATA_DESCRIPTION, fieldName);
        throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, errDesc, fieldName);
    }

    public static void raiseMissingDataException(String fieldName, String fieldValue)
            throws DataValidationException {
        String errMessage = DataValidationException.MANDATORY_MISSING_MESSAGE.format(fieldValue);
        String errDesc = String.format(ErrorDescriptionConstants.MANDATORY_PARAMETER_MISSING_DESCRIPTION, fieldName);
        throw new DataValidationException(errMessage, ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING,
                errDesc, fieldName);
    }

}
