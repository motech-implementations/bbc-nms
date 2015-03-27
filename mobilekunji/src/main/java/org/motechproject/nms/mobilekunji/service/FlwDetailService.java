package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.FlwDetail;

/**
 * The purpose of this class is to provide methods to create, delete, find and update the
 * Service Consumption of Flw.
 */
public interface FlwDetailService {

    /**
     * Creates the object for FlwDetail
     *
     * @param record
     * @return FlwDetail
     */
    public FlwDetail create(FlwDetail record);

    /**
     * Updates the Flw Details
     *
     * @param record
     * @return FlwDetail
     */
    public FlwDetail update(FlwDetail record);

    /**
     * Deletes the FlwDetail record
     *
     * @param record
     */
    public void delete(FlwDetail record);

    /**
     * Finds service consumption details of Flw by its Id
     *
     * @param nmsFlwId
     * @return FlwDetail
     */
    public FlwDetail findServiceConsumptionByNmsFlwId(Long nmsFlwId);

    /**
     * Finds the service consumption details of Flw by its msisdn
     *
     * @param msisdn
     * @return FlwDetail
     */
    public FlwDetail findServiceConsumptionByMsisdn(String msisdn);

}
