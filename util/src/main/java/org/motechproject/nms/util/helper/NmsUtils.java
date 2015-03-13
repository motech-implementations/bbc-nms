package org.motechproject.nms.util.helper;

import org.joda.time.DateTime;

/**
 * Utility class to provide static utility methods
 * that would be common for all the modules
 */
public final class NmsUtils {

    private NmsUtils() {

    }

    /**
     * This is a utility method to return the current time in
     * joda DateTime format
     *
     * @return current timestamp
     */
    public static DateTime getCurrentTimeStamp() {
        return (new DateTime());
    }
}
