package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.FlwDetail;
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
    public FlwDetail create(FlwDetail record) {
        return this.serviceConsumptionFlwRecordDataService.create(record);
    }

    @Override
    public FlwDetail update(FlwDetail record) {
        return this.serviceConsumptionFlwRecordDataService.update(record);
    }

    @Override
    public void delete(FlwDetail record) {
        this.serviceConsumptionFlwRecordDataService.delete(record);
    }

    @Override
    public FlwDetail findServiceConsumptionByNmsFlwId(Long nmsFlwId) {
        return serviceConsumptionFlwRecordDataService.findServiceConsumptionByNmsFlwId(nmsFlwId);
    }

    @Override
    public FlwDetail findServiceConsumptionByMsisdn(String msisdn) {
        return serviceConsumptionFlwRecordDataService.findServiceConsumptionByMsisdn(msisdn);
    }
}
