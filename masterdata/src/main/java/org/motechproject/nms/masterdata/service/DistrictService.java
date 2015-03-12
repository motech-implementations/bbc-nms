package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.District;

/**
 * This interface is used for crud operations on District
 */
public interface DistrictService {

    /**
     * create District type object
     *
     * @param record of the District
     */
    District create(District record);

    /**
     * update District type object
     *
     * @param record of the District
     */
    void update(District record);

    /**
     * delete District type object
     *
     * @param record of the District
     */
    void delete(District record);

    /**
     * delete All State type object
     */
    void deleteAll();

    District findDistrictByParentCode(Long districtCode, Long stateCode);

    District findById(Long id);
}
