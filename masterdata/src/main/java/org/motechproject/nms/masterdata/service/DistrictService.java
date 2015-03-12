package org.motechproject.nms.masterdata.service;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.nms.masterdata.domain.District;

/**
 * Created by abhishek on 12/3/15.
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


    @Lookup
    District findDistrictByParentCode(Long districtCode, Long stateCode);

    District findById(Long id);
}
