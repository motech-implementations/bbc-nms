package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.FlwDetail;

/**
 * The purpose of this class is to provide methods to create, delete, find and update the
 * service level FlwDetail.
 */
public interface ServiceConsumptionFlwService {

    public FlwDetail create(FlwDetail record);

    public FlwDetail update(FlwDetail record);

    public void delete(FlwDetail record);

    public FlwDetail findServiceConsumptionByNmsFlwId(Long nmsFlwId);

}
