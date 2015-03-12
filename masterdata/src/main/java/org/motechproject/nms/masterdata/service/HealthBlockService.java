package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.HealthBlock;

/**
 * Created by root on 17/3/15.
 */
public interface HealthBlockService {

    /**
     * create HealthBlock type object
     *
     * @param record of the Village
     */
    HealthBlock create(HealthBlock record);

    /**
     * update Circle type object
     *
     * @param record of the HealthBlock
     */
    void update(HealthBlock record);

    /**
     * delete HealthBlock type object
     *
     * @param record of the HealthBlock
     */
    void delete(HealthBlock record);

    /**
     *
     * @return
     */
    HealthBlock findHealthBlockByParentCode(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode);

    HealthBlock findById(Long id);


}
