package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.LanguageLocationCodeCsv;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeCsvDataService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeServiceCsv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("languageLocationCodeServiceCsv")
public class LanguageLocationCodeServiceCsvImpl implements LanguageLocationCodeServiceCsv {

    private LanguageLocationCodeCsvDataService languageLocationCodeCsvDataService;

    @Autowired
    public LanguageLocationCodeServiceCsvImpl(LanguageLocationCodeCsvDataService languageLocationCodeCsvDataService) {
        this.languageLocationCodeCsvDataService = languageLocationCodeCsvDataService;
    }

    /**
     * gets LanguageLocationCodeCsv from database by id
     *
     * @param id primary kry of the record
     * @return LanguageLocationCodeCsv type object
     */
    @Override
    public LanguageLocationCodeCsv getRecord(Long id) {
        return languageLocationCodeCsvDataService.findById(id);
    }

    /**
     * deletes LanguageLocationCodeCsv from database
     *
     * @param record LanguageLocationCodeCsv from database
     */
    @Override
    public void delete(LanguageLocationCodeCsv record) {
        languageLocationCodeCsvDataService.delete(record);
    }

}
