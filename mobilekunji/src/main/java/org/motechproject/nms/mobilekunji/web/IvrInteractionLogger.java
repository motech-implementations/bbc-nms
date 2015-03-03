package org.motechproject.nms.mobilekunji.web;

import org.motechproject.nms.mobilekunji.constants.ConfigurationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * The <code>IvrInteractionLogger</code> is used to log IVR interactions.
 */
public class IvrInteractionLogger {

    private static Logger logger = LoggerFactory.getLogger(IvrInteractionLogger.class);

    /**
     * Logs interactions between  IVR System and MK. The log contains
     * parameters separated by '|' :
     *
     * @param startTime  Ivr interaction start time in seconds since Epoch.
     * @param endTime    Ivr interaction end time in seconds since Epoch.
     * @param path       URL of the Ivr interaction.
     * @param resultCode HTTP Status Code of the Mk interaction.
     */
    public static void logIvrInteraction(final long startTime, final long endTime, final String path,
                                         final int resultCode) {

        long start = TimeUnit.SECONDS.convert(startTime, TimeUnit.MILLISECONDS);
        long elapsedTime = endTime - startTime;

        logger.info(start + ConfigurationConstants.LOG_SEPERATOR + elapsedTime + ConfigurationConstants.LOG_SEPERATOR + path
                + ConfigurationConstants.LOG_SEPERATOR + resultCode);
    }
}
