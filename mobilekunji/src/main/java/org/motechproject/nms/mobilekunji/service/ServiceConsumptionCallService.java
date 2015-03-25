package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.CallDetail;

/**
 * The purpose of this class is to provide methods to create, delete and update the service level CallDetail.
 */
public interface ServiceConsumptionCallService {

    public CallDetail create(CallDetail record);

    public CallDetail update(CallDetail record);

    public void delete(CallDetail record);
}
