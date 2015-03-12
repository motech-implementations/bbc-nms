package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.TalukaCsv;

/**
 * Created by abhishek on 12/3/15.
 */
public interface TalukaCsvService {


    /**
     * delete TalukaCsv type object
     *
     * @param record of the TalukaCsv
     */
    void delete(TalukaCsv record);

    /**
     * create TalukaCsv type object
     *
     * @param record of the TalukaCsv
     */
    TalukaCsv create(TalukaCsv record);

    TalukaCsv findById(Long id);
}
