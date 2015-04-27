package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.CsvHealthSubFacility;
import org.motechproject.nms.masterdata.repository.HealthSubFacilityCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.HealthSubFacilityCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on HealthSubFacilityCsv
 */
@Service("healthSubFacilityCsvService")
public class HealthSubFacilityCsvServiceImpl implements HealthSubFacilityCsvService {

    private HealthSubFacilityCsvRecordsDataService healthSubFacilityCsvRecordsDataService;

    @Autowired
    public HealthSubFacilityCsvServiceImpl(HealthSubFacilityCsvRecordsDataService healthSubFacilityCsvRecordsDataService) {
        this.healthSubFacilityCsvRecordsDataService = healthSubFacilityCsvRecordsDataService;
    }


    /**
     * delete HealthSubFacilityCsv type object
     *
     * @param record of the HealthSubFacilityCsv
     */
    @Override
    public void delete(CsvHealthSubFacility record) {
        healthSubFacilityCsvRecordsDataService.delete(record);

    }

    /**
     * create HealthSubFacilityCsv type object
     *
     * @param record of the HealthSubFacilityCsv
     */
    @Override
    public CsvHealthSubFacility create(CsvHealthSubFacility record) {
        return healthSubFacilityCsvRecordsDataService.create(record);
    }

    /**
     * Gets Health Sub Facility Csv details by its Id
     *
     * @param id
     * @return HealthSubFacility
     */
    @Override
    public CsvHealthSubFacility findById(Long id) {
        return healthSubFacilityCsvRecordsDataService.findById(id);
    }


}
