package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.DistrictCsv;
import org.motechproject.nms.masterdata.repository.DistrictCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.DistrictCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on DistrictCsv
 */
@Service("districtCsvService")
public class DistrictCsvServiceImpl implements DistrictCsvService {

    private DistrictCsvRecordsDataService districtCsvRecordsDataService;

    @Autowired
    public DistrictCsvServiceImpl(DistrictCsvRecordsDataService districtCsvRecordsDataService) {
        this.districtCsvRecordsDataService = districtCsvRecordsDataService;
    }

    /**
     * delete DistrictCsv type object
     *
     * @param record of the DistrictCsv
     */
    @Override
    public void delete(DistrictCsv record) {
        districtCsvRecordsDataService.delete(record);
    }

    /**
     * create DistrictCsv type object
     *
     * @param record of the DistrictCsv
     */
    @Override
    public DistrictCsv create(DistrictCsv record) {
        return districtCsvRecordsDataService.create(record);
    }

    /**
     * Gets the District Csv Details by its Id
     * @param id
     * @return DistrictCsv
     */
    @Override
    public DistrictCsv findById(Long id) {
        return districtCsvRecordsDataService.findById(id);
    }
}
