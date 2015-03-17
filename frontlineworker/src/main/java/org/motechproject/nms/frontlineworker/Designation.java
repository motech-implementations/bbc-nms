package org.motechproject.nms.frontlineworker;

/**
 * This enum specified the possible values for Front Line Worker Designation
 */
public enum Designation {

    ANM, AWW, ASHA, USHA;

    //This procedure returns the Designation corresponding to the string provided.
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
