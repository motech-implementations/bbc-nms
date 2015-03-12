package org.motechproject.nms.masterdata.service;


import org.motechproject.nms.masterdata.domain.Village;

/**
 * This interface is used for crud operations on Village
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
