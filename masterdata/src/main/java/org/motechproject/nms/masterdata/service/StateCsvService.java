package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.StateCsv;

/**
 * Created by abhishek on 12/3/15.
 */
public interface StateCsvService {

    /**
     * delete StateCsv type object
     *
     * @param record of the StateCsv
     */
    void delete(StateCsv record);

    /**
     * create StateCsv type object
     *
     * @param record of the StateCsv
     */
    StateCsv create(StateCsv record);

    StateCsv findById(Long id);

}
