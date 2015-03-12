package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.HealthBlock;

/**
 * This interface is used for crud operations on HealthBlock
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
     * Finds the Health Block details by its parent code
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param healthBlockCode
     * @return HealthCode
     */
    HealthBlock findHealthBlockByParentCode(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode);

    /**
     * Finds the health block details by its Id
     * @param id
     * @return HealthBlock
     */
    HealthBlock findById(Long id);


}
