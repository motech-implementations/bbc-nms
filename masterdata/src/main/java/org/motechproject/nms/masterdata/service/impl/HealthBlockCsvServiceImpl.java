package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.HealthBlockCsv;
import org.motechproject.nms.masterdata.repository.HealthBlockCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.HealthBlockCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by root on 17/3/15.
 */

@Service("healthBlockCsvService")
public class HealthBlockCsvServiceImpl implements HealthBlockCsvService {

    private HealthBlockCsvRecordsDataService healthBlockCsvRecordsDataService;

    @Autowired
    public HealthBlockCsvServiceImpl(HealthBlockCsvRecordsDataService healthBlockCsvRecordsDataService) {
        this.healthBlockCsvRecordsDataService = healthBlockCsvRecordsDataService;
    }

    /**
     * delete HealthBlockCsv type object
     *
     * @param record of the HealthBlockCsv
     */
    @Override
    public void delete(HealthBlockCsv record) {
        healthBlockCsvRecordsDataService.delete(record);

    }

    /**
     * create HealthBlockCsv type object
     *
     * @param record of the HealthBlockCsv
     */
    @Override
    public HealthBlockCsv create(HealthBlockCsv record) {
        return healthBlockCsvRecordsDataService.create(record);
    }

    @Override
    public HealthBlockCsv findById(Long id) {
        return healthBlockCsvRecordsDataService.findById(id);
    }


}
