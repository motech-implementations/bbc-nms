package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.VillageCsv;

/**
 * Created by root on 17/3/15.
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
