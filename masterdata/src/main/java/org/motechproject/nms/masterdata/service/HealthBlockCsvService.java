package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.CsvHealthBlock;

/**
 * This interface is used for crud operations on HealthBlockCsv
 */
public interface HealthBlockCsvService {

    /**
     * delete HealthBlockCsv type object
     *
     * @param record of the HealthBlockCsv
     */
    void delete(CsvHealthBlock record);

    /**
     * create HealthBlockCsv type object
     *
     * @param record of the HealthBlockCsv
     */

    CsvHealthBlock create(CsvHealthBlock record);

    /**
     * Finds the health block details by its Id
     *
     * @param id
     * @return HealthBlock
     */
    CsvHealthBlock findById(Long id);
}
