package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.CsvHealthFacility;

/**
 * This interface is used for crud operations on HealthFacilityCsv
 */
public interface HealthFacilityCsvService {

    /**
     * delete HealthFacilityCsv type object
     *
     * @param record of the HealthFacilityCsv
     */
    void delete(CsvHealthFacility record);

    /**
     * create HealthFacilityCsv type object
     *
     * @param record of the HealthFacilityCsv
     */
    CsvHealthFacility create(CsvHealthFacility record);

    /**
     * Finds the health facility details by its Id
     *
     * @param id
     * @return HealthFacility
     */
    CsvHealthFacility findById(Long id);

}
