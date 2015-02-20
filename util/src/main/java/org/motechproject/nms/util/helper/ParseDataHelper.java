    package org.motechproject.nms.util.helper;

    import org.motechproject.nms.util.constants.ErrorCodeConstants;

    import org.joda.time.DateTime;

    import java.text.DateFormat;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.Date;

    import static org.motechproject.nms.util.constants.ErrorCodeConstants.*;

    /**
     * This class provides static helper methods for parsing the data from string to specific types.
     * Helper methods also raises exception if parameters is empty/null or invalid.
     */
    public class ParseDataHelper {

        /** checks if the field value is null or empty
         * @field The value of the field to be checked
         * @return true if null or empty else false
         */
        public static boolean isNullOrEmpty(String field ){
            /* "NULL" with ignore case is also considered as empty */
            return (field==null || "".equals(field.trim()) || "NULL".equalsIgnoreCase(field.trim()));
        }


        public static String parseString(String fieldName, String fieldValue, boolean isMandatory) throws DataValidationException {
            if(isNullOrEmpty(fieldValue)){
                if(isMandatory) {
                    throw new DataValidationException("Missing mandatory data for ["+fieldName+"]", MANDATORY_PARAMETER_MISSING, fieldName);
                } else {
                    return null;
                }
            } else {
                return fieldValue;
            }

        }

        public static DateTime parseDate(String fieldName, String fieldValue, boolean isMandatory) throws DataValidationException {
            try {
                if(parseString(fieldName, fieldValue, isMandatory)==null){
                    return null;
                }else{
                    DateTime dateTime = null;
                    DateFormat dateFormat = new SimpleDateFormat("");
                    DateFormat convertedDateFormat = new SimpleDateFormat("");
                    try {
                        Date date = dateFormat.parse(fieldValue);
                        dateTime = new DateTime(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return dateTime;
                }

            } catch(NumberFormatException e) {
                throw new DataValidationException("Invalid Data for ["+fieldName+"]", INVALID_DATA, fieldName);
            }


        }

            public static Integer parseInt(String fieldName, String fieldValue, boolean isMandatory) throws DataValidationException {
            try {
                if(parseString(fieldName, fieldValue, isMandatory)==null){
                    return null;
                }else {
                    return Integer.parseInt(fieldValue);
                }
            } catch(NumberFormatException e) {
                throw new DataValidationException("Invalid Data for ["+fieldName+"]", INVALID_DATA, fieldName);
            }
        }



        public static Long parseLong(String fieldName, String fieldValue, boolean isMandatory)  throws DataValidationException {
            try {
                if(parseString(fieldName, fieldValue, isMandatory)==null){
                    return null;
                }else {
                    return Long.parseLong(fieldValue);
                }
            } catch (NumberFormatException e) {
                throw new DataValidationException("Invalid Data for ["+fieldName+"]", INVALID_DATA, fieldName);
            }
        }

        public static Boolean parseBoolean(String fieldName, String fieldValue, boolean isMandatory)  throws DataValidationException {
            try {
                if(parseString(fieldName, fieldValue, isMandatory)==null){
                    return null;
                }else {
                    return Boolean.parseBoolean(fieldValue);
                }
            } catch (NumberFormatException e) {
                throw new DataValidationException("Invalid Data for ["+fieldName+"]", INVALID_DATA, fieldName);
            }

        }

    }
