package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.util.helper.DataValidationException;

/**
 * This Interface is used to validate Parent Object.
 */
public interface ValidatorService {

    public void validateHealthBlock(Long stateCode,Long districtCode,Long talukaCode)throws DataValidationException;

    public void validateHealthFacility(Long stateCode,Long districtCode,Long talukaCode,Long healthBlockCode) throws DataValidationException;

    public void validateHealthSubFacility(Long stateCode,Long districtCode,Long talukaCode,Long healthBlockCode,Long healthFacilityCode) throws DataValidationException;

}
