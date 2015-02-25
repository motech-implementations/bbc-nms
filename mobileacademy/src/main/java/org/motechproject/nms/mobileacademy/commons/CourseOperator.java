package org.motechproject.nms.mobileacademy.commons;

/**
 * Enum for course operation type.
 */
public enum CourseOperator {
    ADD, MOD, DEL;

    public static CourseOperator getFor(String type) {
        for (CourseOperator operation : CourseOperator.values()) {
            if (operation.equals(type)) {
                return operation;
            }
        }
        return null;
    }
}
