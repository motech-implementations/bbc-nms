package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.TalukaCsv;
import org.motechproject.nms.masterdata.repository.TalukaCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.TalukaCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on TalukaCsv
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

    /**
     * Gets the TalukaCsv by Id
     * @param id
     * @return TalukaCsv
     */
    @Override
    public TalukaCsv findById(Long id) {
        return talukaCsvRecordsDataService.findById(id);
    }
}
