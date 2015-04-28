package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.repository.HealthSubFacilityRecordsDataService;

/**
 * This interface is used for crud operations on HealthSubFacility
 */
public interface HealthSubFacilityService {
    /**
     * delete HealthSubFacility type object
     *
     * @param record of the HealthSubFacility
     */
    void delete(HealthSubFacility record);

    /**
     * create HealthSubFacility type object
     *
     * @param record of the HealthSubFacility
     */
    HealthSubFacility create(HealthSubFacility record);

    /**
     * update HealthSubFacility type object
     *
     * @param record of the HealthSubFacility
     */
    void update(HealthSubFacility record);

    /**
     * Finds the district details by its Id
     *
     * @param id
     * @return
     */
    HealthSubFacility findById(Long id);

    /**
     * Finds the health sub facility details by its parent code
     *
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param healthBlockCode
     * @param healthFacilityCode
     * @param healthSubFacilityCode
     * @return HealthSubFacility
     */
    HealthSubFacility findHealthSubFacilityByParentCode(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode, Long healthFacilityCode, Long healthSubFacilityCode);


    /**
     * Get HealthSubFacilityRecordsDataService object
     */
    public HealthSubFacilityRecordsDataService getHealthSubFacilityRecordsDataService();
}
