package org.motechproject.nms.kilkariobd.event.handler;

import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.service.OutboundCallDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is used to daily scheduled purge event
 * @author nms
 *
 */
@Component
public class PurgeHandler {
    
    Logger logger = LoggerFactory.getLogger(PurgeHandler.class);
    
    @Autowired
    OutboundCallDetailService outboundCallDetailService; 

    /**
     * This method is used to daily scheduled purge event
     */
    @MotechListener(subjects = Constants.PURGE_RECORDS_EVENT_SUBJECT)
    public void purgeOBDCallEventHandler() {
        logger.debug("Daily scheduled purging start");
        outboundCallDetailService.purgeOutboundCallDetail();
        logger.debug("Daily scheduled purging end");
    }
    
}
