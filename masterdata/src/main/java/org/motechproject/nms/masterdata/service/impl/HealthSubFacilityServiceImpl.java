package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.repository.HealthSubFacilityRecordsDataService;
import org.motechproject.nms.masterdata.service.HealthSubFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by root on 17/3/15.
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
     * @param id
     * @return
     */
    @Override
    public HealthSubFacility findById(Long id) {
        return healthSubFacilityRecordsDataService.findById(id);
    }

    /**
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param healthBlockCode
     * @param healthFacilityCode
     * @param healthSubFacilityCode
     * @return
     */
    @Override
    public HealthSubFacility findHealthSubFacilityByParentCode(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode, Long healthFacilityCode, Long healthSubFacilityCode) {
        return healthSubFacilityRecordsDataService.findHealthSubFacilityByParentCode(stateCode, districtCode, talukaCode, healthBlockCode, healthFacilityCode, healthSubFacilityCode);
    }
}
