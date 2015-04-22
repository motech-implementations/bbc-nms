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
}
