package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.TalukaCsv;

/**
 * This interface is used for crud operations on TalukaCsv
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

    /**
     * Finds the TalukaCsv details by its Id
     *
     * @param id
     * @return TalukaCsv
     */
    TalukaCsv findById(Long id);
}
