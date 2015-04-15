package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.util.helper.DataValidationException;

/**
 * This Interface is used to validate Parent Object.
 */
public interface ValidatorService {

    /**
     * Validate Parent of HealthBlock
     *
     * @param stateCode of the State
     * @param districtCode of the District
     * @param talukaCode of the Taluka
     */
    public void validateHealthBlockParent(Long stateCode,Long districtCode,Long talukaCode)throws DataValidationException;

    /**
     * Validate Parent of HealthFacility
     *
     * @param stateCode of the State
     * @param districtCode of the District
     * @param talukaCode of the Taluka
     * @param healthBlockCode of the HealthBlock
     */
    public void validateHealthFacilityParent(Long stateCode,Long districtCode,Long talukaCode,Long healthBlockCode) throws DataValidationException;

    /**
     * Validate Parent of HealthSubFacility
     *
     * @param stateCode of the State
     * @param districtCode of the District
     * @param talukaCode of the Taluka
     * @param healthBlockCode of the HealthBlock
     * @param healthFacilityCode of the HealthFacility
     */
    public void validateHealthSubFacilityParent(Long stateCode,Long districtCode,Long talukaCode,Long healthBlockCode,Long healthFacilityCode) throws DataValidationException;

}
