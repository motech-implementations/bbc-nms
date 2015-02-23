package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.MctsCsv;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.util.helper.DataValidationException;

/**
 * This interface provides methods to validate locations
 */
public interface LocationValidatorService {
    
    /**
     *  This method is used to fetch state from DB based stateCode
     * 
     *  @param stateCode csv uploaded stateCode
     */
    public State stateConsistencyCheck(Long stateCode) throws DataValidationException;

    /**
     *  This method is used to fetch district from DB based on stateId and districtCode
     * 
     *  @param state State Object
     *  @param districtCode csv uploaded districtCode
     */
    public District districtConsistencyCheck(State state, Long districtCode) throws DataValidationException;
    
    /**
     *  This method is used to fetch Taluka from DB 
     *  based on districtId and talukaCode
     * 
     *  @param district District object
     *  @param talukaCode csv uploaded districtCode
     */
    public Taluka talukaConsistencyCheck(District district, Long talukaCode) throws DataValidationException;
    
    /**
     *  This method is used to fetch Health Block from DB 
     *  based on talukaId and healthBlockCode
     * 
     *  @param talukaCode csv uploaded talukaCode
     *  @param taluka Taluka object
     *  @param talukaCode csv uploaded healthBlockCode
     */
    public HealthBlock healthBlockConsistencyCheck(Long talukaCode,
            Taluka taluka, Long healthBlockCode) throws DataValidationException;


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
            throws DataValidationException;
    
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
            throws DataValidationException;

    /**
     *  This method is used to fetch village from DB based on talukaId and villageCode
     * 
     *  @param talukaCode csv uploaded talukaCode
     *  @param taluka Taluka Object
     *  @param villageCode csv uploaded districtCode
     */

    public Village villageConsistencyCheck(Long talukaCode, Taluka taluka, Long villageCode) throws DataValidationException;

    /**
     * This method validates and map location to subscriber
     * @param mctsCsv MctsCsv type object
     * @param subscriber Subscriber type object
     * @return Subscriber type object
     * @throws DataValidationException
     */
    public Subscriber validateAndMapMctsLocationToSubscriber(MctsCsv mctsCsv,
                                                             Subscriber subscriber) throws DataValidationException;

}
