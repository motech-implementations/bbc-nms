package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.HealthBlockCsv;

/**
 * This interface is used for crud operations on HealthBlockCsv
 */
public interface HealthBlockCsvService {

    /**
     * delete HealthBlockCsv type object
     *
     * @param record of the HealthBlockCsv
     */
    void delete(HealthBlockCsv record);

    /**
     * create HealthBlockCsv type object
     *
     * @param record of the HealthBlockCsv
     */

    HealthBlockCsv create(HealthBlockCsv record);

    /**
     * Finds the health block details by its Id
     * @param id
     * @return HealthBlock
     */
    HealthBlockCsv findById(Long id);
}
