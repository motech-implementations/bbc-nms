package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.repository.HealthBlockRecordsDataService;
import org.motechproject.nms.masterdata.service.HealthBlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by root on 17/3/15.
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
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param healthBlockCode
     * @return
     */
    @Override
    public HealthBlock findHealthBlockByParentCode(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode) {
        return healthBlockRecordsDataService.findHealthBlockByParentCode(stateCode, districtCode, talukaCode, healthBlockCode);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public HealthBlock findById(Long id) {
        return healthBlockRecordsDataService.findById(id);
    }


}
