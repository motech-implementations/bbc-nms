package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeDataService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("languageLocationCodeService")
public class LanguageLocationCodeServiceImpl implements LanguageLocationCodeService {

    @Autowired
    private LanguageLocationCodeDataService languageLocationCodeDataService;

    @Override
    public void create(LanguageLocationCode record) {
        languageLocationCodeDataService.create(record);
    }

    @Override
    public void update(LanguageLocationCode record) {
        languageLocationCodeDataService.update(record);
    }
}
