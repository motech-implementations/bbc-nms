package org.motechproject.nms.kilkariobd.service;

import org.motechproject.nms.kilkariobd.domain.OutboundCallRequest;

import java.util.List;

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

    /**
     * Method to fetch all OutboundCallRequest objects
     * @return List<OutboundCallRequest>
     */
    public List<OutboundCallRequest> retrieveAll();

    /**
     * Method to return count of all records
     * @return records count
     */
    public Long getCount();
}
