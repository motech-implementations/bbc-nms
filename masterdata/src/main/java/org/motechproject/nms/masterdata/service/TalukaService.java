package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.Taluka;

/**
 * Created by abhishek on 12/3/15.
 */
public interface TalukaService {

    /**
     * create Taluka type object
     *
     * @param record of the Taluka
     */
    Taluka create(Taluka record);

    /**
     * update Circle type object
     *
     * @param record of the Taluka
     */
    void update(Taluka record);

    /**
     * delete Taluka type object
     *
     * @param record of the Taluka
     */
    void delete(Taluka record);

    /**
     * delete All Taluka type object
     */
    void deleteAll();


    Taluka findTalukaByParentCode(Long stateCode,
                                  Long districtCode,
                                  Long talukaCode);

    Taluka findById(Long id);
}
