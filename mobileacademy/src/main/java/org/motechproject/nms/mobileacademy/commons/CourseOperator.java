package org.motechproject.nms.mobileacademy.commons;

/**
 * Enumeration for course operation type i.e add/modify/delete
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
