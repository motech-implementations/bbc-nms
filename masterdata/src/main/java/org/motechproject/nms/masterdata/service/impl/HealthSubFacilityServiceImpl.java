package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.repository.HealthSubFacilityRecordsDataService;
import org.motechproject.nms.masterdata.service.HealthSubFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on HealthSubFacility
 */
@Service("healthSubFacilityService")
public class HealthSubFacilityServiceImpl implements HealthSubFacilityService {

    private HealthSubFacilityRecordsDataService healthSubFacilityRecordsDataService;

    @Autowired
    public HealthSubFacilityServiceImpl(HealthSubFacilityRecordsDataService healthSubFacilityRecordsDataService) {
        this.healthSubFacilityRecordsDataService = healthSubFacilityRecordsDataService;
    }

    /**
     * delete HealthSubFacility type object
     *
     * @param record of the HealthSubFacility
     */
    @Override
    public void delete(HealthSubFacility record) {
        healthSubFacilityRecordsDataService.delete(record);

    }

    /**
     * create HealthSubFacility type object
     *
     * @param record of the HealthSubFacility
     */
    @Override
    public HealthSubFacility create(HealthSubFacility record) {
        return healthSubFacilityRecordsDataService.create(record);
    }

    /**
     * update HealthSubFacility type object
     *
     * @param record of the HealthSubFacility
     */
    @Override
    public void update(HealthSubFacility record) {
        healthSubFacilityRecordsDataService.update(record);
    }

    /**
     * Gets the Health Sub Facility Details by Id
     *
     * @param id
     * @return HealthSubFacility
     */
    @Override
    public HealthSubFacility findById(Long id) {
        return healthSubFacilityRecordsDataService.findById(id);
    }

    /**
     * Gets the Health Sub Facility Details by its parent code
     *
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param healthBlockCode
     * @param healthFacilityCode
     * @param healthSubFacilityCode
     * @return HealthSubFacility
     */
    @Override
    public HealthSubFacility findHealthSubFacilityByParentCode(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode, Long healthFacilityCode, Long healthSubFacilityCode) {
        return healthSubFacilityRecordsDataService.findHealthSubFacilityByParentCode(stateCode, districtCode, talukaCode, healthBlockCode, healthFacilityCode, healthSubFacilityCode);
    }
}
