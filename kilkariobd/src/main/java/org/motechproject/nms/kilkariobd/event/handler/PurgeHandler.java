package org.motechproject.nms.kilkariobd.event.handler;

import static org.motechproject.nms.kilkariobd.commons.Constants.PREPARE_OBD_TARGET_EVENT_SUBJECT;

import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkariobd.service.OutboundCallDetailService;
import org.springframework.beans.factory.annotation.Autowired;

public class PurgeHandler {
    
    /**
     * This method defines a daily event to be raised by scheduler to prepare target file.
     */
    @Autowired
    OutboundCallDetailService outboundCallDetailService; 
    
    @MotechListener(subjects = PREPARE_OBD_TARGET_EVENT_SUBJECT)
    public void purgeOBDCallEventHandler() {
        
        outboundCallDetailService.purgeOutboundCallDetail();
        
    }
    
}
