package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.LanguageLocationCode;

public interface LanguageLocationCodeService {



    /**
     * creates LanguageLocationCode in database
     *
     * @param record of LanguageLocationCode
     */
    void create(LanguageLocationCode record);

    /**
     * updates LanguageLocationCode in database
     *
     * @param record of LanguageLocationCode
     */
    void update(LanguageLocationCode record);


    /**
     * deletes LanguageLocationCode from database
     *
     * @param record of LanguageLocationCode
     */
    void delete(LanguageLocationCode record);

    /**
     * This method returns the language location code record for a location
     *
     * @param stateCode    code of the state
     * @param districtCode code of the district
     * @return returns null if record not found, else return the languagelocationCode object.
     */
    LanguageLocationCode getRecordByLocationCode(Long stateCode, Long districtCode);

    /**
     * This method returns the value of  MA language location code for a location (state, district)
     *
     * @param stateCode    code of the state
     * @param districtCode code of the district
     * @return null if a  LanguageLocationCode is not determined for location or no entry for location,
     * else returns the determined languageLocationCode value
     */
    Integer getLanguageLocationCodeMAByLocationCode(Long stateCode, Long districtCode);

    /**
     * This method returns the value of MA language location code for a circle
     *
     * @param circleCode code of the circle for which language location code is to determined
     * @return null if a unique LanguageLocationCode is not determined for Circle or no entry for circle,
     * else returns the determined languageLocationCode value
     */
    Integer getLanguageLocationCodeMAByCircleCode(String circleCode);

    /**
     * This method returns the value of default MA language location code for a circle
     *
     * @param circleCode code of the circle for which default language location code is to determined
     * @return null if unique Default LanguageLocationCode is not found or no entry found for circle,
     * else returns the determined value of default Language Location Code.
     */
    Integer getDefaultLanguageLocationCodeMAByCircleCode(String circleCode);

    /**
     * This method returns the value of  MK language location code for a location (state, district)
     *
     * @param stateCode    code of the state
     * @param districtCode code of the district
     * @return null if a  LanguageLocationCode is not determined for location or no entry for location,
     * else returns the determined languageLocationCode value
     */
    Integer getLanguageLocationCodeMKByLocationCode(Long stateCode, Long districtCode);

    /**
     * This method returns the value of MK language location code for a circle
     *
     * @param circleCode code of the circle for which language location code is to determined
     * @return null if a unique LanguageLocationCode is not determined for Circle or no entry for circle,
     * else returns the determined languageLocationCode value
     */
    Integer getLanguageLocationCodeMKByCircleCode(String circleCode);

    /**
     * This method returns the value of default MK language location code for a circle
     *
     * @param circleCode code of the circle for which default language location code is to determined
     * @return null if unique Default LanguageLocationCode is not found or no entry found for circle,
     * else returns the determined value of default Language Location Code.
     */
    Integer getDefaultLanguageLocationCodeMKByCircleCode(String circleCode);

    /**
     * This method returns the value of  KK language location code for a location (state, district)
     *
     * @param stateCode    code of the state
     * @param districtCode code of the district
     * @return null if a  LanguageLocationCode is not determined for location or no entry for location,
     * else returns the determined languageLocationCode value
     */
    Integer getLanguageLocationCodeKKByLocationCode(Long stateCode, Long districtCode);

    /**
     * This method returns the value of KK language location code for a circle
     *
     * @param circleCode code of the circle for which language location code is to determined
     * @return null if a unique LanguageLocationCode is not determined for Circle or no entry for circle,
     * else returns the determined languageLocationCode value
     */
    Integer getLanguageLocationCodeKKByCircleCode(String circleCode);

    /**
     * This method returns the value of default KK language location code for a circle
     *
     * @param circleCode code of the circle for which default language location code is to determined
     * @return null if unique Default LanguageLocationCode is not found or no entry found for circle,
     * else returns the determined value of default Language Location Code.
     */
    Integer getDefaultLanguageLocationCodeKKByCircleCode(String circleCode);

}
