package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on District
 */
@Service("districtService")
public class DistrictServiceImpl implements DistrictService {

    private DistrictRecordsDataService districtRecordsDataService;

    @Autowired
    public DistrictServiceImpl(DistrictRecordsDataService districtRecordsDataService) {
        this.districtRecordsDataService = districtRecordsDataService;
    }

    /**
     * create District type object
     *
     * @param record of the District
     */
    @Override
    public District create(District record) {
        return districtRecordsDataService.create(record);
    }

    /**
     * update District type object
     *
     * @param record of the District
     */
    @Override
    public void update(District record) {
        districtRecordsDataService.update(record);
    }

    /**
     * delete District type object
     *
     * @param record of the District
     */
    @Override
    public void delete(District record) {
        districtRecordsDataService.delete(record);
    }

    /**
     * delete All State type object
     */
    @Override
    public void deleteAll() {
        districtRecordsDataService.deleteAll();
    }

    /**
     * Gets the District Details by its parent code
     * @param districtCode
     * @param stateCode
     * @return District
     */
    @Override
    public District findDistrictByParentCode(Long districtCode, Long stateCode) {
        return null;
    }

    /**
     * Gets the District details by its Id
     * @param id
     * @return District
     */
    @Override
    public District findById(Long id) {
        return districtRecordsDataService.findById(id);
    }
}
