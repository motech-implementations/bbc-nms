package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.HealthSubFacilityCsv;

/**
 * Created by root on 17/3/15.
 */
public interface HealthSubFacilityCsvService {
    /**
     * delete HealthSubFacilityCsv type object
     *
     * @param record of the HealthSubFacilityCsv
     */
    void delete(HealthSubFacilityCsv record);

    /**
     * create HealthSubFacilityCsv type object
     *
     * @param record of the HealthSubFacilityCsv
     */
    HealthSubFacilityCsv create(HealthSubFacilityCsv record);

    /**
     *
     * @param id
     * @return
     */
    HealthSubFacilityCsv findById(Long id);


}
