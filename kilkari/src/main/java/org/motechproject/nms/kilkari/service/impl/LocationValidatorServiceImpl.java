package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.MctsCsv;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.service.LocationValidatorService;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used to validate locations
 */
@Service("locationValidatorService")
public class LocationValidatorServiceImpl implements LocationValidatorService{
    
    @Autowired
    private LocationService locationService;
    
    /**
     *  This method is used to fetch state from DB based stateCode
     * 
     *  @param stateCode csv uploaded stateCode
     */
    public State stateConsistencyCheck(Long stateCode) throws DataValidationException {
        State state = locationService.getStateByCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException("State Code", stateCode.toString());
        }
        return state;
    }

    /**
     *  This method is used to fetch district from DB based on stateId and districtCode
     * 
     *  @param state State Object
     *  @param districtCode csv uploaded districtCode
     */
    public District districtConsistencyCheck(State state, Long districtCode) throws DataValidationException {
        District district = locationService.getDistrictByCode(state.getId(), districtCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException("District Code", districtCode.toString());
        }
        return district;
    }
    
    /**
     *  This method is used to fetch Taluka from DB 
     *  based on districtId and talukaCode
     * 
     *  @param district District object
     *  @param talukaCode csv uploaded districtCode
     */
    public Taluka talukaConsistencyCheck(District district, Long talukaCode) throws DataValidationException {
        Taluka taluka = null;
        if (talukaCode != null) {
            taluka = locationService.getTalukaByCode(district.getId(), talukaCode);
            if (taluka == null) {
                ParseDataHelper.raiseInvalidDataException("Taluka Code", talukaCode.toString());
            }
        }
        return taluka;
    }
    
    /**
     *  This method is used to fetch Health Block from DB 
     *  based on talukaId and healthBlockCode
     * 
     *  @param talukaCode csv uploaded talukaCode
     *  @param taluka Taluka object
     *  @param talukaCode csv uploaded healthBlockCode
     */
    public HealthBlock healthBlockConsistencyCheck(Long talukaCode,
            Taluka taluka, Long healthBlockCode) throws DataValidationException {
        HealthBlock healthBlock = null;
        if (healthBlockCode != null) {
            if (taluka != null) {
                healthBlock = locationService.getHealthBlockByCode(taluka.getId(), healthBlockCode);
                if (healthBlock == null) {
                    ParseDataHelper.raiseInvalidDataException("Block Code", healthBlockCode.toString());
                }
            } else {
                ParseDataHelper.raiseMissingDataException("Taluka Code", talukaCode.toString());
            }
        }
        return healthBlock;
    }


    /**
     *  This method is used to fetch HealthFacility(phc) from DB 
     *  based on healthBloackId and phcCode
     * 
     *  @param healthBlockCode csv uploaded healthBlockCode
     *  @param healthBlock HealthBlock Object
     *  @param phcCode csv uploaded phcCode
     */
    public HealthFacility phcConsistencyCheck(Long healthBlockCode, 
            HealthBlock healthBlock, Long phcCode)
            throws DataValidationException {
        HealthFacility healthFacility = null;
        if (phcCode != null) {
            if (healthBlock != null) {
                healthFacility = locationService.getHealthFacilityByCode(healthBlock.getId(), phcCode);
                if (healthFacility == null) {
                    ParseDataHelper.raiseInvalidDataException("Phc Code", phcCode.toString());
                }
            } else {
                ParseDataHelper.raiseMissingDataException("Block Code", healthBlockCode.toString()); //Missing Block ID
            }
        }
        return healthFacility;
    }
    
    /**
     *  This method is used to fetch HealthSubFacility(subCenter) from DB 
     *  based on healthFacilityId(phc) and HealthSubFacilityCode(subCenterCode)
     * 
     *  @param phcCode csv uploaded phcCode
     *  @param healthFacility HealthFacility Object
     *  @param subCenterCode csv uploaded subCenterCode
     */
    public HealthSubFacility subCenterCodeCheck(Long phcCode,
            HealthFacility healthFacility, Long subCenterCode)
            throws DataValidationException {
        HealthSubFacility healthSubFacility = null;
        if (subCenterCode != null) {
            if (healthFacility != null) {
                healthSubFacility = locationService.getHealthSubFacilityByCode(healthFacility.getId(), subCenterCode);
                if (healthSubFacility == null) {
                    ParseDataHelper.raiseInvalidDataException("Sub centered Code", subCenterCode.toString());
                }
            } else {
                ParseDataHelper.raiseMissingDataException("Phc Code", phcCode.toString());
            }
        }
        return healthSubFacility;
    }

    /**
     *  This method is used to fetch village from DB based on talukaId and villageCode
     * 
     *  @param talukaCode csv uploaded talukaCode
     *  @param taluka Taluka Object
     *  @param villageCode csv uploaded districtCode
     */
    public Village villageConsistencyCheck(Long talukaCode, Taluka taluka, Long villageCode) throws DataValidationException {
        Village village = null;
        if (villageCode != null) {
            if (taluka != null) {
                village = locationService.getVillageByCode(taluka.getId(), villageCode);
                if (village == null) {
                    ParseDataHelper.raiseInvalidDataException("Village Code", villageCode.toString());
                }
            } else {
                ParseDataHelper.raiseMissingDataException("Taluka Code", talukaCode.toString());
            }
        }
        return village;
    }

    /**
     * This method validates and map location to subscriber
     * @param mctsCsv MctsCsv type object
     * @param subscriber Subscriber type object
     * @return Subscriber type object
     * @throws DataValidationException
     */
    @Override
    public Subscriber validateAndMapMctsLocationToSubscriber(MctsCsv mctsCsv,
                                                             Subscriber subscriber) throws DataValidationException {
        
        Long stateCode = ParseDataHelper.validateAndParseLong("State Code", mctsCsv.getStateCode(),  true);
        State state = stateConsistencyCheck(stateCode);

        Long districtCode = ParseDataHelper.validateAndParseLong("District Code", mctsCsv.getDistrictCode(), true);
        District district = districtConsistencyCheck(state, districtCode);

        Long talukaCode = ParseDataHelper.validateAndParseLong("Taluka Code", mctsCsv.getTalukaCode(), false);
        Taluka taluka = talukaConsistencyCheck(district, talukaCode);

        Long healthBlockCode = ParseDataHelper.validateAndParseLong("Health Block Code", mctsCsv.getHealthBlockCode(), false);
        HealthBlock healthBlock = healthBlockConsistencyCheck(talukaCode, taluka, healthBlockCode);

        Long phcCode = ParseDataHelper.validateAndParseLong("Phc Code", mctsCsv.getPhcCode(), false);
        HealthFacility healthFacility = phcConsistencyCheck(healthBlockCode, healthBlock, phcCode);

        Long subCenterCode = ParseDataHelper.validateAndParseLong("Sub centered Code", mctsCsv.getSubCentreCode(), false);
        HealthSubFacility healthSubFacility = subCenterCodeCheck(phcCode, healthFacility, subCenterCode);

        Long villageCode = ParseDataHelper.validateAndParseLong("Village Code", mctsCsv.getVillageCode(), false);
        Village village = villageConsistencyCheck(talukaCode, taluka, villageCode);

        subscriber.setState(state);
        subscriber.setDistrict(district);
        subscriber.setTaluka(taluka);
        subscriber.setHealthBlock(healthBlock);
        subscriber.setPhc(healthFacility);
        subscriber.setSubCentre(healthSubFacility);
        subscriber.setVillage(village);
        return subscriber;
    }
}
