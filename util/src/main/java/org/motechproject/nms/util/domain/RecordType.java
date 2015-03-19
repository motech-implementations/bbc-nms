package org.motechproject.nms.util.domain;

/**
 * Enum for Type Of Record.
 */
public enum RecordType {

    STATE("State"),
    DISTRICT("District"),
    TALUKA("Taluka"),
    HEALTH_BLOCK("Health Block"),
    HEALTH_FACILITY("Health Facility"),
    HEALTH_SUB_FACILITY("Health Sub Facility"),
    VILLAGE("Village"),
    CIRCLE("Circle"),
    OPERATOR("Operator"),
    LANGUAGE_LOCATION_CODE("Language Location Code"),
    MOTHER_MCTS("Mother Mcts"),
    CHILD_MCTS("Child Mcts"),
    FRONT_LINE_WORKER("Front Line Worker"),
    CONTENT_UPLOAD_KK("Content Upload Kilkari"),
    CONTENT_UPLOAD_MK("Content Upload Mobile Kunji"),
    COURSE_CONTENT("Course Content");

    private final String recordType;

    private RecordType(String recordType) {
        this.recordType = recordType;
    }

    /**
     * This method returns the String value corresponding to the enum
     * @return String The String value corresponding to the enum
     */
    @Override
    public String toString() {
        return recordType;
    }
}
