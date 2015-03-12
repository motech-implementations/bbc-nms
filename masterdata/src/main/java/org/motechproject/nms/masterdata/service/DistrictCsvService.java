package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.DistrictCsv;

/**
 * This interface is used for crud operations on DistrictCsv
 */
public interface DistrictCsvService {

    /**
     * delete DistrictCsv type object
     *
     * @param record of the DistrictCsv
     */
    void delete(DistrictCsv record);

    /**
     * create DistrictCsv type object
     *
     * @param record of the DistrictCsv
     */
    DistrictCsv create(DistrictCsv record);

    DistrictCsv findById(Long id);
}
