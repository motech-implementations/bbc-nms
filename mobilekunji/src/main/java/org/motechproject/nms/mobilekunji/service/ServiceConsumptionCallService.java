package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionCall;

/**
 * Created by abhishek on 13/3/15.
 */
public interface ServiceConsumptionCallService {

    public ServiceConsumptionCall create(ServiceConsumptionCall record);

    public ServiceConsumptionCall update(ServiceConsumptionCall record);

    public void delete(ServiceConsumptionCall record);
}
