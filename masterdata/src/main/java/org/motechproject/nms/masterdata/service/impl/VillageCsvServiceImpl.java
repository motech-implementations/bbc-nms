package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.VillageCsv;
import org.motechproject.nms.masterdata.repository.VillageCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.VillageCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by root on 17/3/15.
 */
@Service("villageCsvService")
public class VillageCsvServiceImpl implements VillageCsvService {

    private VillageCsvRecordsDataService villageCsvRecordsDataService;

    @Autowired
    public VillageCsvServiceImpl(VillageCsvRecordsDataService villageCsvRecordsDataService)
    {
        this.villageCsvRecordsDataService=villageCsvRecordsDataService;
    }
    /**
     * delete VillageCsv type object
     *
     * @param record of the VillageCsv
     */
    @Override
    public void delete(VillageCsv record) {
        villageCsvRecordsDataService.delete(record);

    }

    /**
     * create VillageCsv type object
     *
     * @param record of the VillageCsv
     */
    @Override
    public VillageCsv create(VillageCsv record) {
        return villageCsvRecordsDataService.create(record);
    }

    @Override
    public VillageCsv findById(Long id) {
        return villageCsvRecordsDataService.findById(id);
    }
}
