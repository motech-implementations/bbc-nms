package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.HealthSubFacilityCsv;

/**
 * This interface is used for crud operations on HealthSubFacilityCsv
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
     * Finds the health sub facility details by its Id
     *
     * @param id
     * @return
     */
    HealthSubFacilityCsv findById(Long id);


}
