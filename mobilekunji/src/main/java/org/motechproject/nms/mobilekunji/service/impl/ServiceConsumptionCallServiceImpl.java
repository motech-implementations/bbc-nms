package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.CallDetail;
import org.motechproject.nms.mobilekunji.repository.ServiceConsumptionCallRecordDataService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of ServiceConsumptionCallService
 */

@Service("serviceConsumptionCallService")
public class ServiceConsumptionCallServiceImpl implements ServiceConsumptionCallService {

    private ServiceConsumptionCallRecordDataService serviceConsumptionRecordDataService;

    @Autowired
    public ServiceConsumptionCallServiceImpl(ServiceConsumptionCallRecordDataService serviceConsumptionRecordDataService) {
        this.serviceConsumptionRecordDataService = serviceConsumptionRecordDataService;
    }

    @Override
    public CallDetail create(CallDetail record) {
        return this.serviceConsumptionRecordDataService.create(record);
    }

    @Override
    public CallDetail update(CallDetail record) {
        return this.serviceConsumptionRecordDataService.update(record);
    }

    @Override
    public void delete(CallDetail record) {
        this.serviceConsumptionRecordDataService.delete(record);
    }
}
