package org.motechproject.nms.kilkariobd.service.impl;


import org.motechproject.nms.kilkariobd.domain.OutboundCallRequest;
import org.motechproject.nms.kilkariobd.mapper.CSVMapper;
import org.motechproject.nms.kilkariobd.repository.OutboundCallRequestDataService;
import org.motechproject.nms.kilkariobd.service.OutboundCallRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * Method to fetch all OutboundCallRequest objects
     * @return List<OutboundCallRequest>
     */
    @Override
    public List<OutboundCallRequest> retrieveAll() {
        return requestDataService.retrieveAll();
    }

    /**
     * Method to return count of all records
     * @return records count
     */
    @Override
    public Long getCount() {
        return requestDataService.count();
    }

    /**
     * This method exports the records from OutboundCallRequest
     * @param fileName Complete file name to which to export
     * @return returns the count of records exported
     */
    @Override
    public Long exportOutBoundCallRequest(String fileName) {
        List<OutboundCallRequest> callRequests = retrieveAll();
        Long recordCount = getCount();
        CSVMapper.writeByCsvMapper(fileName, callRequests);
        return recordCount;
    }
}
