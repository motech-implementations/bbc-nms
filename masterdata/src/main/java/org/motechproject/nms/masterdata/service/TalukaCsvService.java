package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.CsvTaluka;

/**
 * This interface is used for crud operations on TalukaCsv
 */
public interface TalukaCsvService {


    /**
     * delete TalukaCsv type object
     *
     * @param record of the TalukaCsv
     */
    void delete(CsvTaluka record);

    /**
     * create TalukaCsv type object
     *
     * @param record of the TalukaCsv
     */
    CsvTaluka create(CsvTaluka record);

    /**
     * Finds the TalukaCsv details by its Id
     *
     * @param id
     * @return TalukaCsv
     */
    CsvTaluka findById(Long id);
}
