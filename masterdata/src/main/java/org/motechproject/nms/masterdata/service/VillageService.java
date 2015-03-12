package org.motechproject.nms.masterdata.service;


import org.motechproject.nms.masterdata.domain.Village;

/**
 * Created by nms on 17/3/15.
 */
public interface VillageService {
    /**
     * create Village type object
     *
     * @param record of the Village
     */
    Village create(Village record);

    /**
     * update Circle type object
     *
     * @param record of the Village
     */
    void update(Village record);

    /**
     * delete Village type object
     *
     * @param record of the Village
     */
    void delete(Village record);

    Village findVillageByParentCode(Long stateCode,
                                  Long districtCode,
                                  Long talukaCode,
                                  Long villageCode);
}
