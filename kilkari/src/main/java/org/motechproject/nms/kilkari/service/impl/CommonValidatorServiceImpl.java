package org.motechproject.nms.kilkari.service.impl;

import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.kilkari.domain.AbortionType;
import org.motechproject.nms.kilkari.domain.CsvMcts;
import org.motechproject.nms.kilkari.domain.EntryType;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.service.CommonValidatorService;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.masterdata.service.OperatorService;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used to validate locations
 */
@Service("commonValidatorService")
public class CommonValidatorServiceImpl implements CommonValidatorService {
    
    @Autowired
    private LocationService locationService;


    @Autowired
    private OperatorService operatorService;

    @Autowired
    private CircleService circleService;
    
    @Autowired
    private LanguageLocationCodeService llcService;

    @Autowired
    private LanguageLocationCodeService languageLocationCodeService;

    private static Logger logger = LoggerFactory.getLogger(CommonValidatorService.class);

    /**
     *  This method is used to fetch state from DB based stateCode
     * 
     *  @param stateCode csv uploaded stateCode
     */
    public State checkAndGetState(Long stateCode) throws DataValidationException {
        State state = locationService.getStateByCode(stateCode);
        if (state == null) {
            ParseDataHelper.raiseInvalidDataException(Constants.STATE_CODE, stateCode.toString());
        }
        return state;
    }

    /**
     *  This method is used to fetch district from DB based on stateId and districtCode
     * 
     *  @param state State Object
     *  @param districtCode csv uploaded districtCode
     */
    public District checkAndGetDistrict(State state, Long districtCode) throws DataValidationException {
        District district = locationService.getDistrictByCode(state.getId(), districtCode);
        if (district == null) {
            ParseDataHelper.raiseInvalidDataException(Constants.DISTRICT_CODE, districtCode.toString());
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
    public Taluka checkAndGetTaluka(District district, Long talukaCode) throws DataValidationException {
        Taluka taluka = null;
        if (talukaCode != null) {
            taluka = locationService.getTalukaByCode(district.getId(), talukaCode);
            if (taluka == null) {
                ParseDataHelper.raiseInvalidDataException(Constants.TALUKA_CODE, talukaCode.toString());
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
    public HealthBlock checkAndGetHealthBlock(Long talukaCode,
                                              Taluka taluka, Long healthBlockCode) throws DataValidationException {
        HealthBlock healthBlock = null;
        if (healthBlockCode != null) {
            if (taluka != null) {
                healthBlock = locationService.getHealthBlockByCode(taluka.getId(), healthBlockCode);
                if (healthBlock == null) {
                    ParseDataHelper.raiseInvalidDataException(Constants.HEALTH_BLOCK_CODE, healthBlockCode.toString());
                }
            } else {
                ParseDataHelper.raiseMissingDataException(Constants.TALUKA_CODE, talukaCode.toString());
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
    public HealthFacility checkAndGetPhc(Long healthBlockCode,
                                         HealthBlock healthBlock, Long phcCode)
            throws DataValidationException {
        HealthFacility healthFacility = null;
        if (phcCode != null) {
            if (healthBlock != null) {
                healthFacility = locationService.getHealthFacilityByCode(healthBlock.getId(), phcCode);
                if (healthFacility == null) {
                    ParseDataHelper.raiseInvalidDataException(Constants.PHC_CODE, phcCode.toString());
                }
            } else {
                ParseDataHelper.raiseMissingDataException(Constants.HEALTH_BLOCK_CODE, healthBlockCode.toString()); //Missing Block ID
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
    public HealthSubFacility checkAndGetSubCenter(Long phcCode,
                                                  HealthFacility healthFacility, Long subCenterCode)
            throws DataValidationException {
        HealthSubFacility healthSubFacility = null;
        if (subCenterCode != null) {
            if (healthFacility != null) {
                healthSubFacility = locationService.getHealthSubFacilityByCode(healthFacility.getId(), subCenterCode);
                if (healthSubFacility == null) {
                    ParseDataHelper.raiseInvalidDataException(Constants.SUB_CENTERED_CODE, subCenterCode.toString());
                }
            } else {
                ParseDataHelper.raiseMissingDataException(Constants.PHC_CODE, phcCode.toString());
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
    public Village checkAndGetVillage(Long talukaCode, Taluka taluka, Long villageCode) throws DataValidationException {
        Village village = null;
        if (villageCode != null) {
            if (taluka != null) {
                village = locationService.getVillageByCode(taluka.getId(), villageCode);
                if (village == null) {
                    ParseDataHelper.raiseInvalidDataException(Constants.VILLAGE_CODE, villageCode.toString());
                }
            } else {
                ParseDataHelper.raiseMissingDataException(Constants.TALUKA_CODE, talukaCode.toString());
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
     * @throws NmsInternalServerError 
     */
    @Override
    public Subscriber validateAndMapMctsLocationToSubscriber(CsvMcts mctsCsv,
                                                             Subscriber subscriber) throws DataValidationException, NmsInternalServerError {
        
        Long stateCode = ParseDataHelper.validateAndParseLong(Constants.STATE_CODE, mctsCsv.getStateCode(),  true);
        State state = checkAndGetState(stateCode);

        Long districtCode = ParseDataHelper.validateAndParseLong(Constants.DISTRICT_CODE, mctsCsv.getDistrictCode(), true);
        District district = checkAndGetDistrict(state, districtCode);

        Long talukaCode = ParseDataHelper.validateAndParseLong(Constants.TALUKA_CODE, mctsCsv.getTalukaCode(), false);
        Taluka taluka = checkAndGetTaluka(district, talukaCode);

        Long healthBlockCode = ParseDataHelper.validateAndParseLong(Constants.HEALTH_BLOCK_CODE, mctsCsv.getHealthBlockCode(), false);
        HealthBlock healthBlock = checkAndGetHealthBlock(talukaCode, taluka, healthBlockCode);

        Long phcCode = ParseDataHelper.validateAndParseLong(Constants.PHC_CODE, mctsCsv.getPhcCode(), false);
        HealthFacility healthFacility = checkAndGetPhc(healthBlockCode, healthBlock, phcCode);

        Long subCenterCode = ParseDataHelper.validateAndParseLong(Constants.SUB_CENTERED_CODE, mctsCsv.getSubCentreCode(), false);
        HealthSubFacility healthSubFacility = checkAndGetSubCenter(phcCode, healthFacility, subCenterCode);

        Long villageCode = ParseDataHelper.validateAndParseLong(Constants.VILLAGE_CODE, mctsCsv.getVillageCode(), false);
        Village village = checkAndGetVillage(talukaCode, taluka, villageCode);

        subscriber.setState(state);
        subscriber.setStateCode(stateCode);
        subscriber.setLanguageLocationCode(getLLCCodeByStateDistrict(stateCode, districtCode).
                getLanguageLocationCode());
        subscriber.setDistrict(district);
        subscriber.setTaluka(taluka);
        subscriber.setHealthBlock(healthBlock);
        subscriber.setPhc(healthFacility);
        subscriber.setSubCentre(healthSubFacility);
        subscriber.setVillage(village);
        return subscriber;
    }
    
    /**
     * This method is used to LanguageLocationCode based on State and District
     * @param stateCode
     * @param districtCode
     * @return
     * @throws NmsInternalServerError
     */
    @Override
    public LanguageLocationCode getLLCCodeByStateDistrict(
            Long stateCode, Long districtCode) throws NmsInternalServerError {
        LanguageLocationCode llcCodeRecord = llcService.getRecordByLocationCode(stateCode, districtCode);
        if (llcCodeRecord != null) {
            return llcCodeRecord;
        } else {
            String errMessage = "languageLocationCode could not be determined for stateCode : "
                    + stateCode +" and districtCode " + districtCode;
            throw new NmsInternalServerError(errMessage, ErrorCategoryConstants.INCONSISTENT_DATA, errMessage);

        }
    }

    /**
     * Validates the entry type value and raises exception if not valid
     * @param entryType string to be validated
     * @throws DataValidationException
     */
    @Override
    public void checkValidEntryType(String entryType) throws DataValidationException {
        if (entryType!=null) {
            boolean foundEntryType = EntryType.checkValidEntryType(entryType);
            if(!foundEntryType){
                ParseDataHelper.raiseInvalidDataException(Constants.ENTRY_TYPE, entryType);
            }
        }
    }

    /**
     * Validates the Abortion type value and raises exception if not valid
     * @param abortion string to be validated
     * @throws DataValidationException
     */
    @Override
    public void checkValidAbortionType(String abortion) throws DataValidationException {
        if(abortion!=null){
            boolean foundAbortionType = AbortionType.checkValidAbortionType(abortion);
            if(!foundAbortionType){
                ParseDataHelper.raiseInvalidDataException(Constants.ABORTION, abortion);
            }
        }
    }

    /**
     * This method validate the operatorCode from database
     * @param operatorCode code of the operator
     * @throws DataValidationException
     */
    @Override
    public void validateOperator(String operatorCode) throws DataValidationException {
        if (operatorCode != null) {
            Operator operator = operatorService.getRecordByCode(operatorCode);
            if (operator == null) {
                String errMessage = String.format(DataValidationException.INVALID_FORMAT_MESSAGE, Constants.OPERATOR_CODE, operatorCode);
                String errDesc = String.format(ErrorDescriptionConstants.INVALID_API_PARAMETER_DESCRIPTION, Constants.OPERATOR_CODE);
                throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, errDesc, Constants.OPERATOR_CODE);
            }
        }
    }

    /**
     * This method validate the circleCode from database
     * @param circleCode code of the circle
     * @throws DataValidationException
     */
    @Override
    public void validateCircle(String circleCode) throws DataValidationException {
        if (circleCode != null) {
            Circle circle = circleService.getRecordByCode(circleCode);
            if (circle == null) {
                String errMessage = String.format(DataValidationException.INVALID_FORMAT_MESSAGE, Constants.CIRCLE_CODE, circleCode);
                String errDesc = String.format(ErrorDescriptionConstants.INVALID_API_PARAMETER_DESCRIPTION, Constants.CIRCLE_CODE);
                throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, errDesc, Constants.CIRCLE_CODE);
            }
        }
    }

    /**
     * This method validate the languageLocationCode from database
     * @param llcCode value of the languageLocationCode
     * @throws DataValidationException
     */
    @Override
    public void validateLanguageLocationCode(String llcCode) throws DataValidationException {
        if (llcCode != null) {
            LanguageLocationCode languageLocationCode = languageLocationCodeService.findLLCByCode(llcCode);
            if (languageLocationCode == null) {
                String errMessage = String.format(DataValidationException.INVALID_FORMAT_MESSAGE, Constants.LANGUAGE_LOCATION_CODE, llcCode);
                String errDesc = String.format(ErrorDescriptionConstants.INVALID_API_PARAMETER_DESCRIPTION, Constants.LANGUAGE_LOCATION_CODE);
                throw new DataValidationException(errMessage, ErrorCategoryConstants.INVALID_DATA, errDesc, Constants.LANGUAGE_LOCATION_CODE);
            }
        }
    }

    /**
     * This method is used to check if the weeks passed from given date are less than given duration (weeks)
     * @param lmpOrDob
     * @return boolean if less
     * @throws DataValidationException if more
     */
    public void validateWeeksFromDate(DateTime lmpOrDob, Integer durationInWeek, String fieldName) throws DataValidationException {
        DateTime currDate = DateTime.now();

        int weeks = Weeks.weeksBetween(lmpOrDob, currDate).getWeeks();

        logger.debug(String.format("Weeks from date : %s with current date %s : %d", currDate.toString(), lmpOrDob.toString(), weeks));

        if ( weeks >= durationInWeek) {
            throw new DataValidationException("Date too Old for subscription", ErrorCategoryConstants.INCONSISTENT_DATA,
                    "Date too old for Subscription", fieldName);
        }
    }


}
