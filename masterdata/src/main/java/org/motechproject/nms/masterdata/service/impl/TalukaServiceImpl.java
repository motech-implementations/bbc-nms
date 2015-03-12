package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.repository.TalukaRecordsDataService;
import org.motechproject.nms.masterdata.service.TalukaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 12/3/15.
 */

@Service("talukaService")
public class TalukaServiceImpl implements TalukaService {

    private TalukaRecordsDataService talukaRecordsDataService;

    @Autowired
    public TalukaServiceImpl(TalukaRecordsDataService talukaRecordsDataService) {
        this.talukaRecordsDataService = talukaRecordsDataService;
    }

    /**
     * create Taluka type object
     *
     * @param record of the Taluka
     */
    @Override
    public Taluka create(Taluka record) {
        return talukaRecordsDataService.create(record);
    }

    /**
     * update Circle type object
     *
     * @param record of the Taluka
     */
    @Override
    public void update(Taluka record) {
        talukaRecordsDataService.update(record);
    }

    /**
     * delete Taluka type object
     *
     * @param record of the Taluka
     */
    @Override
    public void delete(Taluka record) {
        talukaRecordsDataService.delete(record);
    }

    /**
     * delete All Taluka type object
     */
    @Override
    public void deleteAll() {
        talukaRecordsDataService.deleteAll();
    }

    @Override
    public Taluka findTalukaByParentCode(Long stateCode, Long districtCode, Long talukaCode) {
        return talukaRecordsDataService.findTalukaByParentCode(stateCode,districtCode,talukaCode);
    }

    @Override
    public Taluka findById(Long id) {
        return talukaRecordsDataService.findById(id);
    }
}
