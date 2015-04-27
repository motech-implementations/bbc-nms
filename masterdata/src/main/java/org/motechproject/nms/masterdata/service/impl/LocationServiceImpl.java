package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on Location
 */
@Service("locationService")
public class LocationServiceImpl implements LocationService {

    private StateService stateService;

    private DistrictService districtService;

    private TalukaService talukaService;

    private HealthBlockService healthBlockService;

    private VillageService villageService;

    private HealthFacilityService healthFacilityService;

    private HealthSubFacilityService healthSubFacilityService;

    @Autowired
    public LocationServiceImpl(StateService stateService, DistrictService districtService, TalukaService talukaService, HealthBlockService healthBlockService, VillageService villageService, HealthFacilityService healthFacilityService, HealthSubFacilityService healthSubFacilityService) {
        this.stateService = stateService;
        this.districtService = districtService;
        this.talukaService = talukaService;
        this.healthBlockService = healthBlockService;
        this.villageService = villageService;
        this.healthFacilityService = healthFacilityService;
        this.healthSubFacilityService = healthSubFacilityService;
    }

    /**
     * validates the Location by state Id and District Id
     *
     * @param stateId
     * @param districtId
     * @return True/False
     */
    @Override
    public boolean validateLocation(Long stateId, Long districtId) {

        boolean flag = false;

        State state = stateService.findById(stateId);
        District district = districtService.findById(districtId);

        if (null != state && null != district) {

            District districtData = districtService.findDistrictByParentCode(district.getStateCode(), district.getDistrictCode());

            if (null != districtData) {
                flag = true;
                return flag;
            }
        }
        return flag;
    }

    /**
     * get State record for given State Census code
     *
     * @param stateCode State Census Code
     * @return State object corresponding to the census code
     */
    @Override
    public State getStateByCode(Long stateCode) {

        return stateService.findRecordByStateCode(stateCode);
    }

    /**
     * get District record for given District Census code
     *
     * @param stateId      MDS Generated id of the state record in which to look up for district
     * @param districtCode District Census Code
     * @return District object corresponding to the Census code
     */
    @Override
    public District getDistrictByCode(Long stateId, Long districtCode) {

        District district = null;

        if (null != stateId && null != districtCode) {
            State state = stateService.findById(stateId);

            if (null != state) {
                district = districtService.findDistrictByParentCode(districtCode, state.getStateCode());
                return district;
            }
        }
        return district;
    }

    /**
     * get Taluka record for given Taluka Census (or Proposed Census) Code
     *
     * @param districtId MDS Generated id of the District record, in which to look up for Taluka
     * @param talukaCode Taluka Census (or Proposed Census) Code
     * @return Taluka object corresponding to the Census (or Proposed Census)code
     */
    @Override
    public Taluka getTalukaByCode(Long districtId, Long talukaCode) {

        Taluka taluka = null;

        if (null != districtId && null != talukaCode) {
            District district = districtService.findById(districtId);
            if (null != district) {
                taluka = talukaService.findTalukaByParentCode(district.getStateCode(),
                        district.getDistrictCode(), talukaCode);
                return taluka;
            }
        }
        return taluka;
    }

    /**
     * get HealthBlock record for given HealthBlock Census (or Proposed Census) Code
     *
     * @param talukaId        MDS Generated id of the Taluka record, in which to look up for HealthBlock
     * @param healthBlockCode HealthBlock Census (or Proposed Census) Code
     * @return HealthBlock object corresponding to the Census (or Proposed Census)code
     */
    @Override
    public HealthBlock getHealthBlockByCode(Long talukaId, Long healthBlockCode) {

        HealthBlock healthBlock = null;

        if (null != talukaId && null != healthBlockCode) {
            Taluka taluka = talukaService.findById(talukaId);
            if (null != taluka) {

                healthBlock = healthBlockService.findHealthBlockByParentCode(taluka.getStateCode(), taluka.getDistrictCode(), taluka.getTalukaCode(), healthBlockCode);
                return healthBlock;
            }
        }
        return healthBlock;
    }

    /**
     * get HealthFacility record for given HealthFacility Census (or Proposed Census) Code
     *
     * @param healthBlockId      MDS Generated id of the HealthBlock record, in which to look up for HealthFacility
     * @param healthFacilityCode HealthFacility Census (or Proposed Census) Code
     * @return HealthFacility object corresponding to the Census (or Proposed Census)code
     */
    @Override
    public HealthFacility getHealthFacilityByCode(Long healthBlockId, Long healthFacilityCode) {

        HealthFacility healthFacility = null;

        if (null != healthBlockId && null != healthFacilityCode) {
            HealthBlock healthBlock = healthBlockService.findById(healthBlockId);

            if (null != healthBlock) {

                healthFacility = healthFacilityService.findHealthFacilityByParentCode(healthBlock.getStateCode(),
                        healthBlock.getDistrictCode(), healthBlock.getTalukaCode(), healthBlock.getHealthBlockCode(),
                        healthFacilityCode);
                return healthFacility;
            }
        }
        return healthFacility;
    }

    /**
     * get HealthSubFacility record for given HealthSubFacility Census (or Proposed Census) Code
     *
     * @param healthFacilityId      MDS Generated id of the HealthFacility record, in which to look up for HealthSubFacility
     * @param healthSubFacilityCode HealthSubFacility Census (or Proposed Census) Code
     * @return HealthSubFacility object corresponding to the Census (or Proposed Census)code
     */
    @Override
    public HealthSubFacility getHealthSubFacilityByCode(Long healthFacilityId, Long healthSubFacilityCode) {

        HealthSubFacility healthSubFacility = null;

        if (null != healthFacilityId && null != healthSubFacilityCode) {
            HealthFacility healthFacility = healthFacilityService.findById(healthFacilityId);

            if (null != healthFacility) {

                healthSubFacility = healthSubFacilityService.findHealthSubFacilityByParentCode(healthFacility.getStateCode(),
                        healthFacility.getDistrictCode(), healthFacility.getTalukaCode(), healthFacility.getHealthBlockCode(),
                        healthFacility.getHealthFacilityCode(), healthSubFacilityCode);
                return healthSubFacility;
            }
        }
        return healthSubFacility;
    }

    /**
     * get Village record for given Village Census (or MCTS ) Code
     *
     * @param talukaId    MDS Generated id of the Taluka record, in which to look up for Village
     * @param villageCode Village Census (or MCTS ) Code
     * @return Village object corresponding to the Census (or MCTS )code
     */
    @Override
    public Village getVillageByCode(Long talukaId, Long villageCode) {

        Village village = null;

        if (null != talukaId && null != villageCode) {
            Taluka taluka = talukaService.findById(talukaId);

            if (null != taluka) {
                village = villageService.findVillageByParentCode(taluka.getStateCode(), taluka.getDistrictCode(), taluka.getTalukaCode(), villageCode);
                return village;
            }
        }
        return village;
    }

    /**
     * get Integer for given State Census (or MCTS ) Code
     *
     * @param stateCode State Census (or MCTS ) Code
     * @return Integer object corresponding to the Census (or MCTS )code
     */
    @Override
    public Integer getMaCappingByCode(Long stateCode) {
        return getStateByCode(stateCode).getMaCapping();
    }

    /**
     * get Integer value for given State Census (or MCTS ) Code
     *
     * @param stateCode State Census (or MCTS ) Code
     * @return Integer object corresponding to the Census (or MCTS )code
     */
    @Override
    public Integer getMkCappingByCode(Long stateCode) {

        return getStateByCode(stateCode).getMkCapping();
    }

    /**
     * Check whether MK is deployed on the the State or not by its State Code
     *
     * @param stateCode
     * @return true/false
     */
    @Override
    public Boolean getMkServiceDeployedByCode(Long stateCode) {
        State state = getStateByCode(stateCode);
        if(null != state) {
            return state.getIsMkDeployed();
        }
       return null;
    }

    /**
     * Check whether MA is deployed on the the State or not by its State Code
     *
     * @param stateCode
     * @return true/false
     */
    @Override
    public Boolean getMaServiceDeployedByCode(Long stateCode) {
        State state = getStateByCode(stateCode);
        if(null != state) {
            return state.getIsMaDeployed();
        }
        return null;
    }

    /**
     * Check whether KK is deployed on the the State or not by its State Code
     *
     * @param stateCode
     * @return true/false
     */
    @Override
    public Boolean getKkServiceDeployedByCode(Long stateCode) {
        State state = getStateByCode(stateCode);
        if(null != state) {
            return state.getIsKkDeployed();
        }
        return null;
    }

    /**
     * Check whether White List for msisdn is enabled on the the State or not by its State Code
     *
     * @param stateCode
     * @return true/false
     */
    @Override
    public Boolean getWhiteListingEnableStatusByCode(Long stateCode) {
        State state = getStateByCode(stateCode);
        if(null != state) {
            return state.getIsWhiteListEnable();
        }
        return null;
    }


}
