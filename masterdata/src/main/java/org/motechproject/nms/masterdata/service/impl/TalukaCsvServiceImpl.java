package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.CsvTaluka;
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
    public void delete(CsvTaluka record) {
        this.talukaCsvRecordsDataService.delete(record);
    }

    /**
     * create TalukaCsv type object
     *
     * @param record of the TalukaCsv
     */
    @Override
    public CsvTaluka create(CsvTaluka record) {
        return talukaCsvRecordsDataService.create(record);
    }

    /**
     * Gets the TalukaCsv by Id
     *
     * @param id
     * @return TalukaCsv
     */
    @Override
    public CsvTaluka findById(Long id) {
        return talukaCsvRecordsDataService.findById(id);
    }
}
