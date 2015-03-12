package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.repository.HealthBlockRecordsDataService;
import org.motechproject.nms.masterdata.service.HealthBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on HealthBlock
 */
@Service("healthBlockService")
public class HealthBlockServiceImpl implements HealthBlockService {

    private HealthBlockRecordsDataService healthBlockRecordsDataService;

    @Autowired
    public HealthBlockServiceImpl(HealthBlockRecordsDataService healthBlockRecordsDataService) {
        this.healthBlockRecordsDataService = healthBlockRecordsDataService;
    }


    /**
     * create HealthBlock type object
     *
     * @param record of the HealthBlock
     */
    @Override
    public HealthBlock create(HealthBlock record) {
        return healthBlockRecordsDataService.create(record);
    }

    /**
     * update Circle type object
     *
     * @param record of the HealthBlock
     */
    @Override
    public void update(HealthBlock record) {
        healthBlockRecordsDataService.update(record);
    }

    /**
     * delete HealthBlock type object
     *
     * @param record of the HealthBlock
     */
    @Override
    public void delete(HealthBlock record) {
        healthBlockRecordsDataService.delete(record);
    }

    /**
     * Gets the Health Block details by tis parents code
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param healthBlockCode
     * @return HealthBlock
     */
    @Override
    public HealthBlock findHealthBlockByParentCode(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode) {
        return healthBlockRecordsDataService.findHealthBlockByParentCode(stateCode, districtCode, talukaCode, healthBlockCode);
    }

    /**
     * Gets the Health Block Details by its Id
     * @param id
     * @return HealthBlock
     */
    @Override
    public HealthBlock findById(Long id) {
        return healthBlockRecordsDataService.findById(id);
    }


}
