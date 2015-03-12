package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.HealthFacilityCsv;
import org.motechproject.nms.masterdata.repository.HealthFacilityCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.HealthFacilityCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 12/3/15.
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
    public void delete(HealthFacilityCsv record) {
        healthFacilityCsvRecordsDataService.delete(record);
    }

    /**
     * create HealthFacilityCsv type object
     *
     * @param record of the HealthFacilityCsv
     */
    @Override
    public HealthFacilityCsv create(HealthFacilityCsv record) {
        return healthFacilityCsvRecordsDataService.create(record);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public HealthFacilityCsv findById(Long id) {
        return healthFacilityCsvRecordsDataService.findById(id);
    }
}
