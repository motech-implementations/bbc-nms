package org.motechproject.nms.kilkariobd.service;

import org.motechproject.nms.kilkariobd.domain.OutboundCallDetail;

/**
 * Interface to expose the CRUD operations for OutboundCallDetail Entity
 */
public interface OutboundCallDetailService {

    /**
     * Method to create record of type OutboundCallDetail in database
     * @param record OutboundCallDetail type object
     * @return OutboundCallDetail type object
     */
    public OutboundCallDetail create(OutboundCallDetail record);

    /**
     * This method is used to delete OutboundCallDetail of those subscriber 
     * whose subscription have completed or deactivated n days earlier. 
     * Where n is configurable in Kilkari and also used to trigger perge event 
     * of Kilkari which will delete corresponding subscription and subscriber.
     */
    void purgeOutboundCallDetail();
}
