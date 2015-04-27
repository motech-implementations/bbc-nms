package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.CsvDistrict;

/**
 * This interface is used for crud operations on DistrictCsv
 */
public interface DistrictCsvService {

    /**
     * delete DistrictCsv type object
     *
     * @param record of the DistrictCsv
     */
    void delete(CsvDistrict record);

    /**
     * create DistrictCsv type object
     *
     * @param record of the DistrictCsv
     */
    CsvDistrict create(CsvDistrict record);

    /**
     * Finds the district details by its Id
     *
     * @param id
     * @return District
     */
    CsvDistrict findById(Long id);
}
