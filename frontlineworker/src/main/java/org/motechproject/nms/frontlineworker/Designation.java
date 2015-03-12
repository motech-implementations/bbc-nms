package org.motechproject.nms.frontlineworker;

/**
 * Created by abhishek on 24/2/15.
 */
public enum Designation {

    ANM, AWW, ASHA, USHA;

    public static Designation getEnum(String s) {
        if (ANM.name().equals(s)) {
            return ANM;
        } else {
            if (AWW.name().equals(s)) {
                return AWW;
            } else {
                if (ASHA.name().equals(s)) {
                    return ASHA;
                } else {
                    if (USHA.name().equals(s)) {
                        return USHA;
                    }
                }
            }
        }

        return null;

    }

}
