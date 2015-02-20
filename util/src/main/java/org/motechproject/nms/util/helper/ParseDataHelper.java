    package org.motechproject.nms.util.helper;

    import org.joda.time.DateTime;
    import org.motechproject.nms.util.constants.ErrorCategoryConstants;

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
         * @return true if null or empty else false
         * @field The value of the field to be checked
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
                    String errMessage = DataValidationException.MANDATORY_MISSING_MESSAGE.format(fieldValue);
                    throw new DataValidationException(errMessage, ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING, fieldName);
                } else {
                    return null;
                }
            } else {
                return fieldValue;
            }

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
            try {
                if (parseString(fieldName, fieldValue, isMandatory) == null) {
                    return null;
                } else {
                    DateTime dateTime = null;
                    DateFormat dateFormat = new SimpleDateFormat("");

                    Date date = dateFormat.parse(fieldValue);
                    dateTime = new DateTime(date);

                    return dateTime;
                }

            } catch (NumberFormatException | ParseException e) {
                String errMessage = DataValidationException.INVALID_FORMAT_MESSAGE.format(fieldValue);
                throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, fieldName, e);
            }
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
            try {
                if (parseString(fieldName, fieldValue, isMandatory) == null) {
                    return null;
                } else {
                    return Integer.parseInt(fieldValue);
                }
            } catch (NumberFormatException e) {

                String errMessage = DataValidationException.INVALID_FORMAT_MESSAGE.format(fieldValue);
                throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, fieldName, e);
            }
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
            try {
                if (parseString(fieldName, fieldValue, isMandatory) == null) {
                    return null;
                } else {
                    return Long.parseLong(fieldValue);
                }
            } catch (NumberFormatException e) {
                String errMessage = DataValidationException.INVALID_FORMAT_MESSAGE.format(fieldValue);
                throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, fieldName, e);
            }
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
            try {
                if (parseString(fieldName, fieldValue, isMandatory) == null) {
                    return null;
                } else {
                    return Boolean.parseBoolean(fieldValue);
                }
            } catch (NumberFormatException e) {
                String errMessage = DataValidationException.INVALID_FORMAT_MESSAGE.format(fieldValue);
                throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, fieldName, e);
            }

        }

    }
