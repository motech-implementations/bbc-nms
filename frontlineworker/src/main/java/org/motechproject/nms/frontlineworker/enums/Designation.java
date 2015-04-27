package org.motechproject.nms.frontlineworker.enums;

/**
 * This enum specifies the possible values for Front Line Worker Designation
 */
public enum Designation {

    ANM, AWW, ASHA, USHA;

    //This procedure returns the Designation corresponding to the string provided.
    public static Designation getEnum(String s) {

        switch (s) {
            case "ANM":
                return ANM;

            case "AWW":
                return AWW;

            case "ASHA":
                return ASHA;

            case "USHA":
                return USHA;

            default:
                return null;
        }
    }

}
