package org.motechproject.nms.kilkariobd.service.impl;


import org.motechproject.nms.kilkariobd.domain.OutboundCallRequest;
import org.motechproject.nms.kilkariobd.repository.OutboundCallRequestDataService;
import org.motechproject.nms.kilkariobd.service.OutboundCallRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class to implement method defined in OutboundCallRequestService Interface
 */
@Service("outboundCallRequestService")
public class OutboundCallRequestServiceImpl implements OutboundCallRequestService {

    @Autowired
    OutboundCallRequestDataService requestDataService;

    /**
     * Method to delete all the records from OutboundCallRequest table
     */
    @Override
    public void deleteAll() {
        requestDataService.deleteAll();
    }

    /**
     * Method to create OutboundCallRequest record in the database
     * @param record OutboundCallRequest
     * @return OutboundCallRequest
     */
    @Override
    public OutboundCallRequest create(OutboundCallRequest record) {
        return requestDataService.create(record);
    }
}
