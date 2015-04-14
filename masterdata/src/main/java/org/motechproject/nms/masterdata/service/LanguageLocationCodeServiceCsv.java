package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.CsvLanguageLocationCode;

/**
 * This interface is used for crud operations on LanguageLocationCodeCsv
 */

public interface LanguageLocationCodeServiceCsv {

    /**
     * gets LanguageLocationCodeCsv from database by id
     *
     * @param id primary kry of the record
     * @return LanguageLocationCodeCsv type object
     */
    CsvLanguageLocationCode getRecord(Long id);

    /**
     * deletes LanguageLocationCodeCsv from database
     *
     * @param record LanguageLocationCodeCsv from database
     */
    void delete(CsvLanguageLocationCode record);

}
