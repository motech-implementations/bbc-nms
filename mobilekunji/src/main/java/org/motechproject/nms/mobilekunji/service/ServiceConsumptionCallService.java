package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionCall;

/**
 * The purpose of this class is to provide methods to create, delete and update the service level ServiceConsumptionCall.
 */
public interface ServiceConsumptionCallService {

    public ServiceConsumptionCall create(ServiceConsumptionCall record);

    public ServiceConsumptionCall update(ServiceConsumptionCall record);

    public void delete(ServiceConsumptionCall record);
}
