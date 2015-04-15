package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.*;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This Class is used to validate Parent Object.
 */
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

    @Override
    public void validateHealthBlock(Long stateCode, Long districtCode, Long talukaCode) throws DataValidationException {

        State state = stateService.findRecordByStateCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException("State", "StateCode");
        }

        District district = districtService.findDistrictByParentCode(districtCode, stateCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException("District", "DistrictCode");
        }

        Taluka taluka = talukaService.findTalukaByParentCode(stateCode, districtCode, talukaCode);

        if (taluka == null) {
            ParseDataHelper.raiseInvalidDataException("Taluka", "TalukaCode");
        }
    }

    @Override
    public void validateHealthFacility(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode) throws DataValidationException{

        State state = stateService.findRecordByStateCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException("State", "StateCode");
        }

        District district = districtService.findDistrictByParentCode(districtCode, stateCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException("District", "DistrictCode");
        }

        Taluka taluka = talukaService.findTalukaByParentCode(stateCode, districtCode, talukaCode);

        if (taluka == null) {
            ParseDataHelper.raiseInvalidDataException("Taluka", "TalukaCode");
        }

        HealthBlock healthBlock = healthBlockService.findHealthBlockByParentCode(
                stateCode, districtCode, talukaCode, healthBlockCode);
        if (healthBlock == null) {
            ParseDataHelper.raiseInvalidDataException("HealthBlock", "HealthBlockCode");
        }
    }

    @Override
    public void validateHealthSubFacility(Long stateCode, Long districtCode, Long talukaCode, Long healthBlockCode, Long healthFacilityCode) throws DataValidationException{

        validateHealthFacility(stateCode,districtCode,talukaCode,healthBlockCode);

        HealthFacility healthFacility = healthFacilityService.findHealthFacilityByParentCode(stateCode, districtCode, talukaCode, healthBlockCode, healthFacilityCode);
        if (healthFacility == null) {
            ParseDataHelper.raiseInvalidDataException("HealthFacility", "HealthFacilityCode");
        }
    }

}
