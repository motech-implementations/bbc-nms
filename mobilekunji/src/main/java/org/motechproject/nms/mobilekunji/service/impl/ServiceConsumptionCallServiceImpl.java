package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionCall;
import org.motechproject.nms.mobilekunji.repository.ServiceConsumptionCallRecordDataService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionCallService;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 13/3/15.
 */

@Service("serviceConsumptionCallService")
public class ServiceConsumptionCallServiceImpl implements ServiceConsumptionCallService {

    private ServiceConsumptionCallRecordDataService serviceConsumptionRecordDataService;

    @Override
    public ServiceConsumptionCall create(ServiceConsumptionCall record) {
        return this.serviceConsumptionRecordDataService.create(record);
    }

    @Override
    public ServiceConsumptionCall update(ServiceConsumptionCall record) {
        return this.serviceConsumptionRecordDataService.update(record);
    }

    @Override
    public void delete(ServiceConsumptionCall record) {
        this.serviceConsumptionRecordDataService.delete(record);
    }
}
