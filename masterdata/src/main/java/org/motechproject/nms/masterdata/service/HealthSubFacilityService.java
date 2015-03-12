package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.HealthSubFacility;

/**
 * Created by root on 17/3/15.
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
     *
     * @param id
     * @return
     */
    HealthSubFacility findById(Long id);

    /**
     *
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param healthBlockCode
     * @param healthFacilityCode
     * @param healthSubFacilityCode
     * @return
     */
    HealthSubFacility findHealthSubFacilityByParentCode(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode, Long healthFacilityCode, Long healthSubFacilityCode);

}
