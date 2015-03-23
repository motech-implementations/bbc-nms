package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionFlw;
import org.motechproject.nms.mobilekunji.repository.ServiceConsumptionFlwRecordDataService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionFlwService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of ServiceConsumptionFlwService
 */

@Service("serviceConsumptionFlwService")
public class ServiceConsumptionFlwServiceImpl implements ServiceConsumptionFlwService {

    private ServiceConsumptionFlwRecordDataService serviceConsumptionFlwRecordDataService;

    @Autowired
    public ServiceConsumptionFlwServiceImpl(ServiceConsumptionFlwRecordDataService serviceConsumptionFlwRecordDataService) {
        this.serviceConsumptionFlwRecordDataService = serviceConsumptionFlwRecordDataService;
    }

    @Override
    public ServiceConsumptionFlw create(ServiceConsumptionFlw record) {
        return this.serviceConsumptionFlwRecordDataService.create(record);
    }

    @Override
    public ServiceConsumptionFlw update(ServiceConsumptionFlw record) {
        return this.serviceConsumptionFlwRecordDataService.update(record);
    }

    @Override
    public void delete(ServiceConsumptionFlw record) {
        this.serviceConsumptionFlwRecordDataService.delete(record);
    }

    @Override
    public ServiceConsumptionFlw findServiceConsumptionByNmsFlwId(Long nmsFlwId) {
        return serviceConsumptionFlwRecordDataService.findServiceConsumptionByNmsFlwId(nmsFlwId);
    }
}
