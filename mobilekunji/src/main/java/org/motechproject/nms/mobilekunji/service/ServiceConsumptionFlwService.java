package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionFrontLineWorker;

/**
 * Created by abhishek on 13/3/15.
 */
public interface ServiceConsumptionFlwService {

    public ServiceConsumptionFrontLineWorker create(ServiceConsumptionFrontLineWorker record);

    public ServiceConsumptionFrontLineWorker update(ServiceConsumptionFrontLineWorker record);

    public void delete(ServiceConsumptionFrontLineWorker record);
}
