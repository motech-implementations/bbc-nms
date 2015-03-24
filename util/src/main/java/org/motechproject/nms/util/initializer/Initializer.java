package org.motechproject.nms.util.initializer;

import org.motechproject.nms.util.constants.Constants;
import org.motechproject.osgi.web.service.ServerLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * The purpose of this class is to Initialize logging configuration for NMS.
 * The default log level is configured to Info for org.motechproject.nms
 */
@Component
public class Initializer {

    private ServerLogService serverLogService;

    private Logger logger = LoggerFactory.getLogger("Initializer.java");

    @Autowired
    public Initializer(ServerLogService serverLogService) {
        this.serverLogService = serverLogService;
    }

    /**
     * This Method is called post construction of Initalizer component and
     * it sets the default log level for org.motechproject.nms to INFO
     */
    @PostConstruct
    public void initializeConfiguration() {
        serverLogService.changeLogLevel(Constants.NMS_PACKAGE_NAME, Constants.NMS_DEFAULT_LOG_LEVEL);
        logger.info("Log Level changed successfully for {} to {}", Constants.NMS_PACKAGE_NAME, Constants.NMS_DEFAULT_LOG_LEVEL);
    }
}
