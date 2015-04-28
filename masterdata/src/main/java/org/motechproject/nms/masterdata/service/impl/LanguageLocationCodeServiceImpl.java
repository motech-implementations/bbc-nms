package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeDataService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jdo.Query;
import java.util.List;

/**
 * This class is used for crud operations on LanguageLocationCode
 */

@Service("languageLocationCodeService")
public class LanguageLocationCodeServiceImpl implements LanguageLocationCodeService {

    private LanguageLocationCodeDataService languageLocationCodeDataService;

    private CircleService circleService;

    @Autowired
    public LanguageLocationCodeServiceImpl(LanguageLocationCodeDataService languageLocationCodeDataService,
                                           CircleService circleService) {
        this.languageLocationCodeDataService = languageLocationCodeDataService;
        this.circleService = circleService;
    }

    /**
     * This class represents the query to find list of service specific languageLocationCode for a circleCode
     */
    private class LanguageLocationCodeQuery implements
            QueryExecution<List<String>> {

        private String circleCode;
        private String resultParamName;

        /**
         * All param Constructor
         *
         * @param circleCode
         * @param resultParamName "languageLocationCodeXX"
         */
        public LanguageLocationCodeQuery(String circleCode, String resultParamName) {
            this.circleCode = circleCode;
            this.resultParamName = resultParamName;
        }

        /**
         * Executes the query
         *
         * @param query
         * @param restriction
         * @return List&lt;String&gt;
         */
        @Override
        public List<String> execute(Query query,
                                     InstanceSecurityRestriction restriction) {
            query.setFilter("circleCode == '" + circleCode + "'");
            query.setResult("DISTINCT " + resultParamName + "");
            return (List<String>) query.execute();
        }
    }

    /**
     * This method executes the LanguagelocationQuery and returns the unique language location code
     *
     * @param query LanguageLocationCode Query
     * @return unique languageLocationCode else null
     */
    private String executeUniqueLanguageLocationCodeQuery(LanguageLocationCodeQuery query) {

        /* get the list of distinct languageLocationCodes for the circle */
        List<String> llcList = languageLocationCodeDataService.executeQuery(query);

        /* If a unique languageLocationCode is found then return it else return null */
        if ((llcList != null) && (llcList.size() == 1)) {
            return llcList.get(0);
        }
        return null;
    }

    /**
     * creates LanguageLocationCode in database
     *
     * @param record of LanguageLocationCode
     */
    @Override
    public LanguageLocationCode create(LanguageLocationCode record) {
        return languageLocationCodeDataService.create(record);
    }

    /**
     * updates LanguageLocationCode in database
     *
     * @param record of LanguageLocationCode
     */
    @Override
    public LanguageLocationCode update(LanguageLocationCode record) {
        return languageLocationCodeDataService.update(record);
    }

    /**
     * deletes LanguageLocationCode from database
     *
     * @param record of LanguageLocationCode
     */
    @Override
    public void delete(LanguageLocationCode record) {
        languageLocationCodeDataService.delete(record);
    }

    /**
     * deletes All LanguageLocationCode from database
     */
    @Override
    public void deleteAll() {
        languageLocationCodeDataService.deleteAll();
    }

    /**
     * This method returns the language location code record for a location
     *
     * @param stateCode    code of the state
     * @param districtCode code of the district
     * @return returns null if record not found, else return the languageLocationCode object.
     */
    @Override
    public LanguageLocationCode getRecordByLocationCode(Long stateCode, Long districtCode) {
        return languageLocationCodeDataService.findByLocationCode(stateCode, districtCode);
    }


    /**
     * This method returns the language location code record for a given circle and LanguageLocationCode
     *
     * @param circleCode  code of the circle
     * @param langLocCode languageLocationCode
     * @return returns null if record not found, else return the languageLocationCode object.
     */
    public LanguageLocationCode getRecordByCircleCodeAndLangLocCode(String circleCode, String langLocCode) {
        List<LanguageLocationCode> locationCodeList = languageLocationCodeDataService.findByCircleCodeAndLangLocCode(circleCode, langLocCode);
        if (locationCodeList != null && !locationCodeList.isEmpty()) {
            return locationCodeList.get(0);
        }
        return null;
    }

    /**
     * This method returns the value of language location code for a location (state, district)
     *
     * @param stateCode    code of the state
     * @param districtCode code of the district
     * @return null if a  LanguageLocationCode is not determined for location or no entry for location,
     * else returns the determined languageLocationCode value
     */
    @Override
    public String getLanguageLocationCodeByLocationCode(Long stateCode, Long districtCode) {

        String llc = null;

        LanguageLocationCode langLocCode = languageLocationCodeDataService.findByLocationCode(stateCode, districtCode);
        if (langLocCode != null) {
            llc = langLocCode.getLanguageLocationCode();
        }

        return llc;
    }

    /**
     * This method returns the value of language location code for a circle
     *
     * @param circleCode code of the circle for which language location code is to determined
     * @return null if a unique LanguageLocationCode is not determined for Circle or no entry for circle,
     * else returns the determined languageLocationCode value
     */
    @Override
    public String getLanguageLocationCodeByCircleCode(final String circleCode) {
        LanguageLocationCodeQuery query = new LanguageLocationCodeQuery(circleCode, "languageLocationCode");
        return executeUniqueLanguageLocationCodeQuery(query);
    }

    /**
     * This method returns the value of default language location code for a circle
     *
     * @param circleCode code of the circle for which default language location code is to determined
     * @return null if unique Default LanguageLocationCode is not found or no entry found for circle,
     * else returns the determined value of default Language Location Code.
     */
    @Override
    public String getDefaultLanguageLocationCodeByCircleCode(String circleCode) {

        String llc = null;
        Circle circle = circleService.getRecordByCode(circleCode);

        if (circle != null) {
            llc = circle.getDefaultLanguageLocationCode();
        }
        return llc;
    }

    @Override
    public LanguageLocationCode findLLCByCode(String code) {
        List<LanguageLocationCode> locationCodeList = languageLocationCodeDataService.findLLCByCode(code);
        if (locationCodeList != null && !locationCodeList.isEmpty()) {
            return locationCodeList.get(0);
        }
        return null;
    }

    /**
     * Get LanguageLocationCodeDataService object
     */
    @Override
    public LanguageLocationCodeDataService getLanguageLocationCodeDataService() {
        return languageLocationCodeDataService;
    }

}
