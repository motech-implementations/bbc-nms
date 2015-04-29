package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.CallDetail;

/**
 * The purpose of this class is to provide methods to create, delete and update the ServiceConsumptionCall.
 */
public interface CallDetailService {


    /**
     * Creates record object for CallDetails
     *
     * @param record
     * @return CallDetail
     */
    public CallDetail create(CallDetail record);

    /**
     * Updates the record of CallDetail
     *
     * @param record
     * @return CallDetail
     */
    public CallDetail update(CallDetail record);

    /**
     * Deletes the CallDetail record
     *
     * @param record
     */
    public void delete(CallDetail record);

    /**
     * Finds the service consumption details of Flw by its msisdn
     *
     * @param callingNumber
     * @return CallDetail
     */
    public CallDetail findCallDetailByCallingNumber(String callingNumber);
}
