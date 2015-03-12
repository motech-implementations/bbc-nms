package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.HealthSubFacilityCsv;
import org.motechproject.nms.masterdata.repository.HealthSubFacilityCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.HealthSubFacilityCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by root on 17/3/15.
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
    public void delete(HealthSubFacilityCsv record) {
        healthSubFacilityCsvRecordsDataService.delete(record);

    }

    /**
     * create HealthSubFacilityCsv type object
     *
     * @param record of the HealthSubFacilityCsv
     */
    @Override
    public HealthSubFacilityCsv create(HealthSubFacilityCsv record) {
        return healthSubFacilityCsvRecordsDataService.create(record);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public HealthSubFacilityCsv findById(Long id) {
        return healthSubFacilityCsvRecordsDataService.findById(id);
    }


}
