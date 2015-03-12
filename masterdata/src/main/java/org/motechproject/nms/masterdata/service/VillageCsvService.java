package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.VillageCsv;

/**
 * This interface is used for crud operations on VillageCsv
 */
public interface VillageCsvService {

    /**
     * delete VillageCsv type object
     *
     * @param record of the VillageCsv
     */
    void delete(VillageCsv record);

    /**
     * create VillageCsv type object
     *
     * @param record of the VillageCsv
     */
    VillageCsv create(VillageCsv record);

    VillageCsv findById(Long id);
}
