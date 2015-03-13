package org.motechproject.nms.util.domain;

/**
 * Enum for Type Of Record.
 */
public enum RecordType {

    DISTRICT("District"),
    CIRCLE("Circle"),
    HEALTH_BLOCK("Health Block"),
    HEALTH_FACILITY("Health Facility"),
    HEALTH_SUB_FACILITY("Health Sub Facility"),
    LANGUAGE_LOCATION_CODE("Language Location Code"),
    LOCATION_UNIT_META_DATA("Location Unit Metadata"),
    OPERATOR("Operator"),
    STATE("State"),
    TALUKA("Taluka"),
    VILLAGE("Village"),
    CHILD_MCTS("Child Mcts"),
    MOTHER_MCTS("Mother Mcts"),
    CONTENT_UPLOAD_KK("Content Upload Kilkari"),
    CONTENT_UPLOAD_MK("Content Upload Mobile Kunji"),
    FRONT_LINE_WORKER("Front Line Worker"),
    COURSE_CONTENT("Course Content");

    private final String recordType;

    private RecordType(String recordType) {
        this.recordType = recordType;
    }
}
