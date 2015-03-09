package org.motechproject.nms.mobileacademy.service.impl;

import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.mobileacademy.service.MasterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This is implementation of Master data service having services from master
 * data module.
 *
 */
@Service("MasterDataService")
public class MasterDataServiceImpl implements MasterDataService {

    @Autowired
    private LanguageLocationCodeService languageLocationCodeService;

    @Autowired
    private CircleService circleService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.MasterDataService#isCircleValid
     * (java.lang.String)
     */
    @Override
    public boolean isCircleValid(String circleCode) {
        Circle circle = circleService.getRecordByCode(circleCode);
        return circle != null ? true : false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.motechproject.nms.mobileacademy.service.MasterDataService#
     * isLlcValidInCircle(java.lang.String, java.lang.Integer)
     */
    @Override
    public boolean isLlcValidInCircle(String circleCode, Integer languageLocCode) {
        LanguageLocationCode languageLocationCode = languageLocationCodeService
                .getRecordByCircleCodeAndLangLocCode(circleCode,
                        languageLocCode);
        return languageLocationCode != null ? true : false;
    }

}
