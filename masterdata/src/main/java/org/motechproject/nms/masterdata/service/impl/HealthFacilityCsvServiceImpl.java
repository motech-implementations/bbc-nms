package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.CsvHealthFacility;
import org.motechproject.nms.masterdata.repository.HealthFacilityCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.HealthFacilityCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on HealthFacilityCsv
 */
@Service("healthFacilityCsvService")
public class HealthFacilityCsvServiceImpl implements HealthFacilityCsvService {

    private HealthFacilityCsvRecordsDataService healthFacilityCsvRecordsDataService;

    @Autowired
    public HealthFacilityCsvServiceImpl(HealthFacilityCsvRecordsDataService healthFacilityCsvRecordsDataService) {
        this.healthFacilityCsvRecordsDataService = healthFacilityCsvRecordsDataService;
    }

    /**
     * delete HealthFacilityCsv type object
     *
     * @param record of the HealthFacilityCsv
     */
    @Override
    public void delete(CsvHealthFacility record) {
        healthFacilityCsvRecordsDataService.delete(record);
    }

    /**
     * create HealthFacilityCsv type object
     *
     * @param record of the HealthFacilityCsv
     */
    @Override
    public CsvHealthFacility create(CsvHealthFacility record) {
        return healthFacilityCsvRecordsDataService.create(record);
    }

    /**
     * Gets the Health Facility Csv Details by its Id
     *
     * @param id
     * @return HealthFacilityCsv
     */
    @Override
    public CsvHealthFacility findById(Long id) {
        return healthFacilityCsvRecordsDataService.findById(id);
    }
}
