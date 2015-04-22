package org.motechproject.nms.kilkariobd.service;

import org.motechproject.nms.kilkariobd.domain.OutboundCallRequest;

/**
 * Interface to expose the CRUD operations for OutboundCallRequest Entity
 */
public interface OutboundCallRequestService {

    /**
     * Method to delete all the records from OutboundCallRequest table
     */
    public void deleteAll();

    /**
     * Method to create OutboundCallRequest record in the database
     * @param record OutboundCallRequest
     * @return OutboundCallRequest
     */
    public OutboundCallRequest create(OutboundCallRequest record);
}
