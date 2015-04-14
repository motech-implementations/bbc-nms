package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.CsvLanguageLocationCode;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeCsvDataService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeServiceCsv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on LanguageLocationCodeCsv
 */

@Service("languageLocationCodeServiceCsv")
public class LanguageLocationCodeCsvServiceImpl implements LanguageLocationCodeServiceCsv {

    private LanguageLocationCodeCsvDataService languageLocationCodeCsvDataService;

    @Autowired
    public LanguageLocationCodeCsvServiceImpl(LanguageLocationCodeCsvDataService languageLocationCodeCsvDataService) {
        this.languageLocationCodeCsvDataService = languageLocationCodeCsvDataService;
    }

    /**
     * gets LanguageLocationCodeCsv from database by id
     *
     * @param id primary kry of the record
     * @return LanguageLocationCodeCsv type object
     */
    @Override
    public CsvLanguageLocationCode getRecord(Long id) {
        return languageLocationCodeCsvDataService.findById(id);
    }

    /**
     * deletes LanguageLocationCodeCsv from database
     *
     * @param record LanguageLocationCodeCsv from database
     */
    @Override
    public void delete(CsvLanguageLocationCode record) {
        languageLocationCodeCsvDataService.delete(record);
    }

}
