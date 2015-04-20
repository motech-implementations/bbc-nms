package org.motechproject.nms.kilkariobd.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkariobd.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.motechproject.nms.kilkariobd.commons.Constants.CONFIGURATION_UPDATE_EVENT;
import static org.motechproject.nms.kilkariobd.commons.Constants.CONFIGURATION_ID;


public class ConfigurationHandler {

    private ConfigurationService configurationService;
    private static Logger logger = LoggerFactory.getLogger(ConfigurationHandler.class);

    @Autowired
    public ConfigurationHandler(ConfigurationService configurationService){

        this.configurationService = configurationService;
    }

    @MotechListener(subjects = CONFIGURATION_UPDATE_EVENT)
    public void configurationUpdate(MotechEvent motechEvent) {

        Map<String, Object> parameters = motechEvent.getParameters();
        List<Long> object_ids = (List<Long>)parameters.get(CONFIGURATION_ID);



    }
}
