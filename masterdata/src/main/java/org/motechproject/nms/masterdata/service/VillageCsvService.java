package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.CsvVillage;

/**
 * This interface is used for crud operations on VillageCsv
 */
public interface VillageCsvService {

    /**
     * delete VillageCsv type object
     *
     * @param record of the VillageCsv
     */
    void delete(CsvVillage record);

    /**
     * create VillageCsv type object
     *
     * @param record of the VillageCsv
     */
    CsvVillage create(CsvVillage record);

    /**
     * Finds the VillageCsv details by its Id
     *
     * @param id
     * @return VillageCsv
     */
    CsvVillage findById(Long id);
}
