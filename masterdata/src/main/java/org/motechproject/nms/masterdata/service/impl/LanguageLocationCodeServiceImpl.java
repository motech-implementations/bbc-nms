package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeDataService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ashish on 18/2/15.
 */
public class LanguageLocationCodeServiceImpl implements LanguageLocationCodeService {

    @Autowired
    private LanguageLocationCodeDataService languageLocationCodeDataService;

    @Override
    public void create(LanguageLocationCode record) {
        languageLocationCodeDataService.create(record);
    }
}
