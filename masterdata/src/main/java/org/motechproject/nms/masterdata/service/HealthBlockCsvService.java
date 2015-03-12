package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.HealthBlockCsv;

/**
 * Created by root on 17/3/15.
 */
public interface HealthBlockCsvService {

    /**
     * delete HealthBlockCsv type object
     *
     * @param record of the HealthBlockCsv
     */
    void delete(HealthBlockCsv record);

    /**
     * create HealthBlockCsv type object
     *
     * @param record of the HealthBlockCsv
     */
    HealthBlockCsv create(HealthBlockCsv record);

    HealthBlockCsv findById(Long id);
}
