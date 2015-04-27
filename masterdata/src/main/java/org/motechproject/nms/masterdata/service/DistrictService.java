package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;

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

    /**
     * Finds the district details by its parent code
     *
     * @param districtCode
     * @param stateCode
     * @return District
     */
    District findDistrictByParentCode(Long districtCode, Long stateCode);

    /**
     * Finds the district details by its Id
     *
     * @param id
     * @return District
     */
    District findById(Long id);

    /**
     * Get DistrictRecordsDataService object
     */
    public DistrictRecordsDataService getDistrictRecordsDataService();
}
