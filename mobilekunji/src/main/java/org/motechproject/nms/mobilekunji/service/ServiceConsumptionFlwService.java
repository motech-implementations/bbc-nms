package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionFlw;

/**
 * Created by abhishek on 13/3/15.
 */
public interface ServiceConsumptionFlwService {

    public ServiceConsumptionFlw create(ServiceConsumptionFlw record);

    public ServiceConsumptionFlw update(ServiceConsumptionFlw record);

    public void delete(ServiceConsumptionFlw record);

    public ServiceConsumptionFlw findServiceConsumptionByNmsFlwId(Long nmsFlwId);

}
