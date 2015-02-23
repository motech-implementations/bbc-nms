package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.LanguageLocationCodeCsv;

public interface LanguageLocationCodeServiceCsv {

    /**
     * gets LanguageLocationCodeCsv from database by id
     *
     * @param id primary kry of the record
     * @return LanguageLocationCodeCsv type object
     */
    LanguageLocationCodeCsv getRecord(Long id);

    /**
     * deletes LanguageLocationCodeCsv from database
     *
     * @param record LanguageLocationCodeCsv from database
     */
    void delete(LanguageLocationCodeCsv record);

}
