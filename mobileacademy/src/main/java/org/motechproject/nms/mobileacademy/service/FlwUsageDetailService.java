package org.motechproject.nms.mobileacademy.service;

import org.motechproject.nms.mobileacademy.domain.FlwUsageDetail;

/**
 * The purpose of this interface is to provide methods to perform operations on
 * FlwUsageDetail table.
 */
public interface FlwUsageDetailService {

    /**
     * create new Flw Usage Record
     * 
     * @param flwUsageDetail
     * @return FlwUsageDetail
     */
    FlwUsageDetail createFlwUsageRecord(FlwUsageDetail flwUsageDetail);

    /**
     * find By Flw Id
     * 
     * @param flwId
     * @return FlwUsageDetail
     */
    FlwUsageDetail findByFlwId(Long flwId);
}