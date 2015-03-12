package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.HealthFacilityCsv;

/**
 * Created by abhishek on 12/3/15.
 */
public interface HealthFacilityCsvService {

    /**
     * delete HealthFacilityCsv type object
     *
     * @param record of the HealthFacilityCsv
     */
    void delete(HealthFacilityCsv record);

    /**
     * create HealthFacilityCsv type object
     *
     * @param record of the HealthFacilityCsv
     */
    HealthFacilityCsv create(HealthFacilityCsv record);

    /**
     *
     * @param id
     * @return
     */
    HealthFacilityCsv findById(Long id);

}
