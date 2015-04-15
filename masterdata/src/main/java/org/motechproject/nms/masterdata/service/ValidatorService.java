package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.util.helper.DataValidationException;

/**
 * This Interface is used to validate Parent Object.
 */
public interface ValidatorService {

    /**
     * update Validate Parent of HealthBlock
     *
     * @param stateCode of the State
     * @param districtCode of the District
     * @param talukaCode of the Taluka
     */
    public void validateHealthBlock(Long stateCode,Long districtCode,Long talukaCode)throws DataValidationException;

    /**
     * update Validate Parent of HealthFacility
     *
     * @param stateCode of the State
     * @param districtCode of the District
     * @param talukaCode of the Taluka
     * @param healthBlockCode of the HealthBlock
     */
    public void validateHealthFacility(Long stateCode,Long districtCode,Long talukaCode,Long healthBlockCode) throws DataValidationException;

    /**
     * update Validate Parent of HealthSubFacility
     *
     * @param stateCode of the State
     * @param districtCode of the District
     * @param talukaCode of the Taluka
     * @param healthBlockCode of the HealthBlock
     * @param healthFacilityCode of the HealthFacility
     */
    public void validateHealthSubFacility(Long stateCode,Long districtCode,Long talukaCode,Long healthBlockCode,Long healthFacilityCode) throws DataValidationException;

}
