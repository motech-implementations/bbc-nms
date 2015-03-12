package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.HealthFacilityCsv;

/**
 * This interface is used for crud operations on HealthFacilityCsv
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
