package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.LanguageLocationCodeCsv;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeCsvDataService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeServiceCsv;
import org.springframework.beans.factory.annotation.Autowired;

public class LanguageLocationCodeServiceCsvImpl implements LanguageLocationCodeServiceCsv {

    @Autowired
    private LanguageLocationCodeCsvDataService languageLocationCodeCsvDataService;

    @Override
    public LanguageLocationCodeCsv findById(Long id) {
        return languageLocationCodeCsvDataService.findById(id);
    }

    @Override
    public void delete(LanguageLocationCodeCsv record) {
        languageLocationCodeCsvDataService.delete(record);
    }
}
