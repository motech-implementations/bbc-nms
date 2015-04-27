package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.repository.TalukaRecordsDataService;
import org.motechproject.nms.masterdata.service.TalukaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on Taluka
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

    /**
     * Gets the Taluka details by its parent code
     *
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @return Taluka
     */
    @Override
    public Taluka findTalukaByParentCode(Long stateCode, Long districtCode, Long talukaCode) {
        return talukaRecordsDataService.findTalukaByParentCode(stateCode, districtCode, talukaCode);
    }

    /**
     * Gets the Taluka details by Id
     *
     * @param id
     * @return Taluka
     */
    @Override
    public Taluka findById(Long id) {
        return talukaRecordsDataService.findById(id);
    }

    /**
     * Get TalukaRecordsDataService object
     */
    @Override
    public TalukaRecordsDataService getTalukaRecordsDataService() {
        return talukaRecordsDataService;
    }
}
