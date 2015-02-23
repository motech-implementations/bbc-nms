package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.service.MDSLookupService;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeDataService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jdo.Query;
import java.util.List;

@Service("languageLocationCodeService")
public class LanguageLocationCodeServiceImpl implements LanguageLocationCodeService {


    @Autowired
    private LanguageLocationCodeDataService languageLocationCodeDataService;

    @Autowired
    private CircleService circleService;

    @Autowired
    private MDSLookupService mdsLookupService;

    /**
     * This class represents the query to find list of service specific languageLocationCode for a circleCode
     */
    private class LanguageLocationCodeQuery implements
            QueryExecution<List<Integer>> {

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

        @Override
        public List<Integer> execute(Query query,
                                     InstanceSecurityRestriction restriction) {
            query.setFilter("circleCode == '" + circleCode + "'");
            query.setResult("DISTINCT " + resultParamName + "");
            return (List<Integer>) query.execute();
        }
    }

    /**
     * This method executes the LanguagelocationQuery and returns the unique language location code
     *
     * @param query LanguageLocationCode Query
     * @return unique languageLocationCode else null
     */
    private Integer executeUniqueLanguageLocationCodeQuery(LanguageLocationCodeQuery query) {

        /* get the list of distinct MA languageLocationCodes for the circle */
        List<Integer> llcList = languageLocationCodeDataService.executeQuery(query);

        /* If a unique languageLocationCode is found then return it else return null */
        if ((llcList != null) && (llcList.size() == 1)) {
            return llcList.get(0);
        }
        return null;
    }

    @Override
    public void create(LanguageLocationCode record) {
        languageLocationCodeDataService.create(record);
    }

    @Override
    public void update(LanguageLocationCode record) {
        languageLocationCodeDataService.update(record);
    }

    @Override
    public void delete(LanguageLocationCode record) {
        languageLocationCodeDataService.delete(record);;
    }

    @Override
    public LanguageLocationCode getRecordByLocationCode(Long stateCode, Long districtCode) {
        return languageLocationCodeDataService.findByLocationCode(stateCode, districtCode);
    }

    @Override
    public Integer getLanguageLocationCodeMAByLocationCode(Long stateCode, Long districtCode) {
        return languageLocationCodeDataService.findByLocationCode(stateCode, districtCode).getLanguageLocationCodeMA();
    }

    @Override
    public Integer getLanguageLocationCodeMAByCircleCode(final String circleCode) {
        LanguageLocationCodeQuery query = new LanguageLocationCodeQuery(circleCode, "languageLocationCodeMA");
        return executeUniqueLanguageLocationCodeQuery(query);
    }

    @Override
    public Integer getDefaultLanguageLocationCodeMAByCircleCode(String circleCode) {
        return circleService.getRecordByCode(circleCode).getDefaultLanguageLocationCodeMA();
    }

    @Override
    public Integer getLanguageLocationCodeMKByLocationCode(Long stateCode, Long districtCode) {
        return languageLocationCodeDataService.findByLocationCode(stateCode, districtCode).getLanguageLocationCodeMK();
    }

    @Override
    public Integer getLanguageLocationCodeMKByCircleCode(String circleCode) {
        LanguageLocationCodeQuery query = new LanguageLocationCodeQuery(circleCode, "languageLocationCodeMA");
        return executeUniqueLanguageLocationCodeQuery(query);
    }

    @Override
    public Integer getDefaultLanguageLocationCodeMKByCircleCode(String circleCode) {
        return circleService.getRecordByCode(circleCode).getDefaultLanguageLocationCodeMK();
    }

    @Override
    public Integer getLanguageLocationCodeKKByLocationCode(Long stateCode, Long districtCode) {
        return languageLocationCodeDataService.findByLocationCode(stateCode, districtCode).getLanguageLocationCodeKK();
    }

    @Override
    public Integer getLanguageLocationCodeKKByCircleCode(String circleCode) {
        LanguageLocationCodeQuery query = new LanguageLocationCodeQuery(circleCode, "languageLocationCodeMA");
        return executeUniqueLanguageLocationCodeQuery(query);
    }

    @Override
    public Integer getDefaultLanguageLocationCodeKKByCircleCode(String circleCode) {
        return circleService.getRecordByCode(circleCode).getDefaultLanguageLocationCodeKK();
    }


}
