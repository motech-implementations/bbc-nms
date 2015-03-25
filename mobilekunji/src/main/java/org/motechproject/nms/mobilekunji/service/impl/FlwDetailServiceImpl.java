package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.FlwDetail;
import org.motechproject.nms.mobilekunji.repository.FlwDetailRecordDataService;
import org.motechproject.nms.mobilekunji.service.FlwDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of FlwDetailService
 */

@Service("flwDetailService")
public class FlwDetailServiceImpl implements FlwDetailService {

    private FlwDetailRecordDataService flwDetailRecordDataService;

    @Autowired
    public FlwDetailServiceImpl(FlwDetailRecordDataService flwDetailRecordDataService) {
        this.flwDetailRecordDataService = flwDetailRecordDataService;
    }

    /**
     * Creates the object for FlwDetail
     *
     * @param record
     * @return FlwDetail
     */
    @Override
    public FlwDetail create(FlwDetail record) {
        return this.flwDetailRecordDataService.create(record);
    }

    /**
     * Updates the Flw Details
     *
     * @param record
     * @return FlwDetail
     */
    @Override
    public FlwDetail update(FlwDetail record) {
        return this.flwDetailRecordDataService.update(record);
    }

    /**
     * Deletes the FlwDetail record
     *
     * @param record
     */
    @Override
    public void delete(FlwDetail record) {
        this.flwDetailRecordDataService.delete(record);
    }

    /**
     * Finds service consumption details of Flw by its Id
     *
     * @param nmsFlwId
     * @return FlwDetail
     */
    @Override
    public FlwDetail findServiceConsumptionByNmsFlwId(Long nmsFlwId) {
        return flwDetailRecordDataService.findServiceConsumptionByNmsFlwId(nmsFlwId);
    }

    /**
     * Finds the service consumption details of Flw by its msisdn
     *
     * @param msisdn
     * @return FlwDetail
     */
    @Override
    public FlwDetail findServiceConsumptionByMsisdn(String msisdn) {
        return flwDetailRecordDataService.findServiceConsumptionByMsisdn(msisdn);
    }

}
