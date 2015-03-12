package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.repository.HealthFacilityRecordsDataService;
import org.motechproject.nms.masterdata.service.HealthFacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by root on 17/3/15.
 */

@Service("healthFacilityService")
public class HealthFacilityServiceImpl implements HealthFacilityService {

    private HealthFacilityRecordsDataService healthFacilityRecordsDataService;

    @Autowired
    public HealthFacilityServiceImpl(HealthFacilityRecordsDataService healthFacilityRecordsDataService) {
        this.healthFacilityRecordsDataService = healthFacilityRecordsDataService;
    }


    /**
     * create HealthFacility type object
     *
     * @param record of the HealthFacility
     */
    @Override
    public HealthFacility create(HealthFacility record) {
        return healthFacilityRecordsDataService.create(record);
    }

    /**
     * update Circle type object
     *
     * @param record of the HealthFacility
     */
    @Override
    public void update(HealthFacility record) {
        healthFacilityRecordsDataService.update(record);

    }

    /**
     * delete HealthFacility type object
     *
     * @param record of the HealthFacility
     */
    @Override
    public void delete(HealthFacility record) {
        healthFacilityRecordsDataService.delete(record);
    }

    /**
     * delete All HealthFacility type object
     */
    @Override
    public void deleteAll() {

    }

    /**
     * @param id
     * @return
     */
    @Override
    public HealthFacility findById(Long id) {
        return healthFacilityRecordsDataService.findById(id);
    }

    @Override
    public HealthFacility findHealthFacilityByParentCode(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode, Long healthFacilityCode) {
        return healthFacilityRecordsDataService.findHealthFacilityByParentCode(stateCode,districtCode,talukaCode,healthBlockCode,healthFacilityCode);
    }
}
