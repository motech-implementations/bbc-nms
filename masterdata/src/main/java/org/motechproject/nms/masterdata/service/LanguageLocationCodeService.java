package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.LanguageLocationCode;

public interface LanguageLocationCodeService {


    /**
     * creates LanguageLocationCode in database
     *
     * @param record of LanguageLocationCode
     */
    LanguageLocationCode create(LanguageLocationCode record);

    /**
     * updates LanguageLocationCode in database
     *
     * @param record of LanguageLocationCode
     */
    LanguageLocationCode update(LanguageLocationCode record);


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
     * This method returns the language location code record for a given circle and LanguageLocationCode
     *
     * @param circleCode  code of the circle
     * @param langLocCode languageLocationCode
     * @return returns null if record not found, else return the languagelocationCode object.
     */
    LanguageLocationCode getRecordByCircleCodeAndLangLocCode(String circleCode, Integer langLocCode);

    /**
     * This method returns the value of  language location code for a location (state, district)
     *
     * @param stateCode    code of the state
     * @param districtCode code of the district
     * @return null if a  LanguageLocationCode is not determined for location or no entry for location,
     * else returns the determined languageLocationCode value
     */
    Integer getLanguageLocationCodeByLocationCode(Long stateCode, Long districtCode);

    /**
     * This method returns the value of language location code for a circle
     *
     * @param circleCode code of the circle for which language location code is to determined
     * @return null if a unique LanguageLocationCode is not determined for Circle or no entry for circle,
     * else returns the determined languageLocationCode value
     */
    Integer getLanguageLocationCodeByCircleCode(String circleCode);

    /**
     * This method returns the value of default language location code for a circle
     *
     * @param circleCode code of the circle for which default language location code is to determined
     * @return null if unique Default LanguageLocationCode is not found or no entry found for circle,
     * else returns the determined value of default Language Location Code.
     */
    Integer getDefaultLanguageLocationCodeByCircleCode(String circleCode);
}
