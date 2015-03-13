package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionFrontLineWorker;
import org.motechproject.nms.mobilekunji.repository.ServiceConsumptionFlwRecordDataService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionFlwService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 13/3/15.
 */

@Service("serviceConsumptionFlwService")
public class ServiceConsumptionFlwServiceImpl implements ServiceConsumptionFlwService {

    private ServiceConsumptionFlwRecordDataService serviceConsumptionFlwRecordDataService;

    @Autowired
    public ServiceConsumptionFlwServiceImpl(ServiceConsumptionFlwRecordDataService serviceConsumptionFlwRecordDataService) {
        this.serviceConsumptionFlwRecordDataService = serviceConsumptionFlwRecordDataService;
    }

    @Override
    public ServiceConsumptionFrontLineWorker create(ServiceConsumptionFrontLineWorker record) {
        return this.serviceConsumptionFlwRecordDataService.create(record);
    }

    @Override
    public ServiceConsumptionFrontLineWorker update(ServiceConsumptionFrontLineWorker record) {
        return this.serviceConsumptionFlwRecordDataService.update(record);
    }

    @Override
    public void delete(ServiceConsumptionFrontLineWorker record) {
        this.serviceConsumptionFlwRecordDataService.delete(record);
    }
}
