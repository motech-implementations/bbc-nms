package org.motechproject.nms.kilkari.service;

import org.joda.time.DateTime;
import org.motechproject.nms.kilkari.domain.CsvMcts;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;

/**
 * This interface provides methods to validate values against
 * Master Data entities
 */
public interface CommonValidatorService {
    
    /**
     *  This method is used to fetch state from DB based stateCode
     * 
     *  @param stateCode csv uploaded stateCode
     */
    public State checkAndGetState(Long stateCode) throws DataValidationException;

    /**
     *  This method is used to fetch district from DB based on stateId and districtCode
     * 
     *  @param state State Object
     *  @param districtCode csv uploaded districtCode
     */
    public District checkAndGetDistrict(State state, Long districtCode) throws DataValidationException;
    
    /**
     *  This method is used to fetch Taluka from DB 
     *  based on districtId and talukaCode
     * 
     *  @param district District object
     *  @param talukaCode csv uploaded districtCode
     */
    public Taluka checkAndGetTaluka(District district, Long talukaCode) throws DataValidationException;
    
    /**
     *  This method is used to fetch Health Block from DB 
     *  based on talukaId and healthBlockCode
     * 
     *  @param talukaCode csv uploaded talukaCode
     *  @param taluka Taluka object
     *  @param talukaCode csv uploaded healthBlockCode
     */
    public HealthBlock checkAndGetHealthBlock(Long talukaCode,
                                              Taluka taluka, Long healthBlockCode) throws DataValidationException;


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
            throws DataValidationException;
    
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
            throws DataValidationException;

    /**
     *  This method is used to fetch village from DB based on talukaId and villageCode
     * 
     *  @param talukaCode csv uploaded talukaCode
     *  @param taluka Taluka Object
     *  @param villageCode csv uploaded districtCode
     */

    public Village checkAndGetVillage(Long talukaCode, Taluka taluka, Long villageCode) throws DataValidationException;

    /**
     * This method validates and map location to subscriber
     * @param mctsCsv MctsCsv type object
     * @param subscriber Subscriber type object
     * @return Subscriber type object
     * @throws DataValidationException
     * @throws NmsInternalServerError 
     */
    public Subscriber validateAndMapMctsLocationToSubscriber(CsvMcts mctsCsv,
                                                             Subscriber subscriber) throws DataValidationException, NmsInternalServerError;

    /**
     * Validates the entry type value and raises exception if not valid
     * @param entryType string to be validated
     * @throws DataValidationException
     */
    void checkValidEntryType(String entryType) throws DataValidationException;

    /**
     * Validates the Abortion type value and raises exception if not valid
     * @param abortion string to be validated
     * @throws DataValidationException
     */
    void checkValidAbortionType(String abortion) throws DataValidationException;


    /**
     * This method validate the operatorCode from database
     * @param operatorCode code of the operator
     * @throws DataValidationException
     */
    void validateOperator(String operatorCode) throws DataValidationException;

    /**
     * This method validate the circleCode from database
     * @param circleCode code of the circle
     * @throws DataValidationException
     */
    void validateCircle(String circleCode) throws DataValidationException;

    /**
     * This method validate the languageLocationCode from database
     * @param llcCode value of the languageLocationCode
     * @throws DataValidationException
     */
    public void validateLanguageLocationCode(String llcCode) throws DataValidationException;

    /**
     * This method is used to LanguageLocationCode based on State and District
     * @param stateCode
     * @param districtCode
     * @return
     * @throws NmsInternalServerError
     */
    LanguageLocationCode getLLCCodeByStateDistrict(Long stateCode,
            Long districtCode) throws NmsInternalServerError;

    /**
     * This method is used to check if the weeks passed from given date are less than given duration (weeks)
     * @param lmpOrDob
     * @throws DataValidationException if more
     */
    public void validateWeeksFromDate(DateTime lmpOrDob, Integer durationInWeek, String fieldName) throws DataValidationException;
}
