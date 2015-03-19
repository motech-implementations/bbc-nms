package org.motechproject.nms.util.constants;

/**
 * This class defines the descriptions corresponding to the error categories.
 * These are generic descriptions and can be used by formatting the string
 * according to requirement.
 * These descriptions details erroneous content in bulk upload record.
 */
public final class ErrorDescriptionConstants {

    public static final String MANDATORY_PARAMETER_MISSING_DESCRIPTION = "Upload unsuccessful as %s is missing.";
    public static final String INVALID_DATA_DESCRIPTION = "Upload unsuccessful as %s is invalid.";
    public static final String CSV_RECORD_MISSING_DESCRIPTION = "Upload unsuccessful as record is missing in CSV.";
    public static final String GENERAL_EXCEPTION_DESCRIPTION = "Upload unsuccessful as some general exception occurred";
    public static final String MISSING_API_PARAMETER_DESCRIPTION = "%s : Not Present";
    public static final String INVALID_API_PARAMETER_DESCRIPTION = "%s : Invalid Value";
    public static final String API_INTERNAL_ERROR_DESCRIPTION = "Internal Error";

    private ErrorDescriptionConstants() {

    }
}
