package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.constants.LocationConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.*;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This Class is used to validate Parent Object.
 */
@Service("validatorService")
public class ValidatorServiceimpl implements ValidatorService {

    private StateService stateService;

    private DistrictService districtService;

    private TalukaService talukaService;

    private HealthBlockService healthBlockService;

    private HealthFacilityService healthFacilityService;

    @Autowired
    public ValidatorServiceimpl(StateService stateService, DistrictService districtService, TalukaService talukaService, HealthBlockService healthBlockService, HealthFacilityService healthFacilityService) {
        this.stateService = stateService;
        this.districtService = districtService;
        this.talukaService = talukaService;
        this.healthBlockService = healthBlockService;
        this.healthFacilityService = healthFacilityService;
    }

    /**
     * Validate Parent of Taluka
     *
     * @param stateCode    of the State
     * @param districtCode of the District
     */
    @Override
    public void validateTalukaParent(Long stateCode, Long districtCode) throws DataValidationException {

        State state = stateService.findRecordByStateCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException(LocationConstants.STATE, String.format(LocationConstants.DATA_VALID_REASON, "StateCode", stateCode));
        }

        District district = districtService.findDistrictByParentCode(districtCode, stateCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException(LocationConstants.DISTRICT, String.format(LocationConstants.DATA_VALID_REASON, "DistrictCode", districtCode));
        }
    }

    /**
     * Validate Parent of HealthBlock
     *
     * @param stateCode    of the State
     * @param districtCode of the District
     * @param talukaCode   of the Taluka
     */
    @Override
    public void validateHealthBlockParent(Long stateCode, Long districtCode, Long talukaCode) throws DataValidationException {

        State state = stateService.findRecordByStateCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException(LocationConstants.STATE, String.format(LocationConstants.DATA_VALID_REASON, "StateCode", stateCode));
        }

        District district = districtService.findDistrictByParentCode(districtCode, stateCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException(LocationConstants.DISTRICT, String.format(LocationConstants.DATA_VALID_REASON, "DistrictCode", districtCode));
        }

        Taluka taluka = talukaService.findTalukaByParentCode(stateCode, districtCode, talukaCode);

        if (taluka == null) {
            ParseDataHelper.raiseInvalidDataException(LocationConstants.TALUKA, String.format(LocationConstants.DATA_VALID_REASON, "TalukaCode", talukaCode));
        }
    }

    /**
     * Validate Parent of HealthFacility
     *
     * @param stateCode       of the State
     * @param districtCode    of the District
     * @param talukaCode      of the Taluka
     * @param healthBlockCode of the HealthBlock
     */
    @Override
    public void validateHealthFacilityParent(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode) throws DataValidationException {

        validateHealthBlockParent(stateCode, districtCode, talukaCode);

        HealthBlock healthBlock = healthBlockService.findHealthBlockByParentCode(
                stateCode, districtCode, talukaCode, healthBlockCode);
        if (healthBlock == null) {
            ParseDataHelper.raiseInvalidDataException(LocationConstants.HEALTH_BLOCK, String.format(LocationConstants.DATA_VALID_REASON, "HealthBlockCode", healthBlockCode));
        }
    }

    /**
     * Validate Parent of HealthSubFacility
     *
     * @param stateCode          of the State
     * @param districtCode       of the District
     * @param talukaCode         of the Taluka
     * @param healthBlockCode    of the HealthBlock
     * @param healthFacilityCode of the HealthFacility
     */
    @Override
    public void validateHealthSubFacilityParent(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode, Long healthFacilityCode) throws DataValidationException {

        validateHealthFacilityParent(stateCode, districtCode, talukaCode, healthBlockCode);

        HealthFacility healthFacility = healthFacilityService.findHealthFacilityByParentCode(stateCode, districtCode, talukaCode, healthBlockCode, healthFacilityCode);
        if (healthFacility == null) {
            ParseDataHelper.raiseInvalidDataException(LocationConstants.HEALTH_FACILITY, String.format(LocationConstants.DATA_VALID_REASON, "HealthFacilityCode", healthFacilityCode));
        }
    }

}
