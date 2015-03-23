package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.ServiceConsumptionFlw;

/**
 * The purpose of this class is to provide methods to create, delete, find and update the
 * service level ServiceConsumptionFlw.
 */
public interface ServiceConsumptionFlwService {

    public ServiceConsumptionFlw create(ServiceConsumptionFlw record);

    public ServiceConsumptionFlw update(ServiceConsumptionFlw record);

    public void delete(ServiceConsumptionFlw record);

    public ServiceConsumptionFlw findServiceConsumptionByNmsFlwId(Long nmsFlwId);

}
