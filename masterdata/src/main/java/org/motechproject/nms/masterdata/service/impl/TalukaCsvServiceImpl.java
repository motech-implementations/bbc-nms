package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.TalukaCsv;
import org.motechproject.nms.masterdata.repository.TalukaCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.TalukaCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 12/3/15.
 */

@Service("talukaCsvService")
public class TalukaCsvServiceImpl implements TalukaCsvService {

    private TalukaCsvRecordsDataService talukaCsvRecordsDataService;

    @Autowired
    public TalukaCsvServiceImpl(TalukaCsvRecordsDataService talukaCsvRecordsDataService) {
        this.talukaCsvRecordsDataService = talukaCsvRecordsDataService;
    }

    /**
     * delete TalukaCsv type object
     *
     * @param record of the TalukaCsv
     */
    @Override
    public void delete(TalukaCsv record) {
        this.talukaCsvRecordsDataService.delete(record);
    }

    /**
     * create TalukaCsv type object
     *
     * @param record of the TalukaCsv
     */
    @Override
    public TalukaCsv create(TalukaCsv record) {
        return talukaCsvRecordsDataService.create(record);
    }

    @Override
    public TalukaCsv findById(Long id) {
        return talukaCsvRecordsDataService.findById(id);
    }
}
