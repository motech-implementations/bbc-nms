package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.CsvHealthSubFacility;

/**
 * This interface is used for crud operations on HealthSubFacilityCsv
 */
public interface HealthSubFacilityCsvService {
    /**
     * delete HealthSubFacilityCsv type object
     *
     * @param record of the HealthSubFacilityCsv
     */
    void delete(CsvHealthSubFacility record);

    /**
     * create HealthSubFacilityCsv type object
     *
     * @param record of the HealthSubFacilityCsv
     */
    CsvHealthSubFacility create(CsvHealthSubFacility record);

    /**
     * Finds the health sub facility details by its Id
     *
     * @param id
     * @return
     */
    CsvHealthSubFacility findById(Long id);


}
