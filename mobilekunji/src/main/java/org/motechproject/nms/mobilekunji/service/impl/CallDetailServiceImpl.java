package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.CallDetail;
import org.motechproject.nms.mobilekunji.repository.CallDetailRecordDataService;
import org.motechproject.nms.mobilekunji.service.CallDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of CallDetailService
 */

@Service("callDetailService")
public class CallDetailServiceImpl implements CallDetailService {

    private CallDetailRecordDataService callDetailRecordDataService;

    @Autowired
    public CallDetailServiceImpl(CallDetailRecordDataService callDetailRecordDataService) {
        this.callDetailRecordDataService = callDetailRecordDataService;
    }

    /**
     * Creates record object for CallDetails
     *
     * @param record
     * @return CallDetail
     */
    @Override
    public CallDetail create(CallDetail record) {
        return callDetailRecordDataService.create(record);
    }

    /**
     * Updates the record of CallDetail
     *
     * @param record
     * @return CallDetail
     */
    @Override
    public CallDetail update(CallDetail record) {
        return callDetailRecordDataService.update(record);
    }

    /**
     * Deletes the CallDetail record
     *
     * @param record
     */
    @Override
    public void delete(CallDetail record) {
        this.callDetailRecordDataService.delete(record);
    }

    /**
     * Finds the service consumption details of Flw by its msisdn
     *
     * @param callingNumber
     * @return CallDetail
     */
    @Override
    public CallDetail findCallDetailByCallingNumber(String callingNumber) {
        return callDetailRecordDataService.findCallDetailByCallingNumber(callingNumber);
    }

}
