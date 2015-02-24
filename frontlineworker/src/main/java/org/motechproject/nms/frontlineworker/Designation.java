package org.motechproject.nms.frontlineworker;

/**
 * Created by abhishek on 24/2/15.
 */
public enum Designation {

    ANM, AWW, ASHA, USHA;

    public static Designation of(String str) {
        for (Designation designation : values()) {
            if (designation.toString().equals(str)) {
                return designation;
            }
        }
        return null;
    }
}
