package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.DistrictCsv;

/**
 * Created by abhishek on 12/3/15.
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
