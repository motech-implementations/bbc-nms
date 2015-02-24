package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.CircleCsv;
import org.motechproject.nms.masterdata.repository.CircleCsvDataService;
import org.motechproject.nms.masterdata.service.CircleCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on CircleCsv
 */

@Service("circleCsvService")
public class CircleCsvServiceImpl implements CircleCsvService {

    private CircleCsvDataService circleCsvDataService;

    @Autowired
    public CircleCsvServiceImpl(CircleCsvDataService circleCsvDataService) {
        this.circleCsvDataService = circleCsvDataService;
    }

    /**
     * gets CircleCsv object based by id
     *
     * @param id primary key of the record
     * @return CircleCsv type object
     */
    @Override
    public CircleCsv getRecord(Long id) {
        return circleCsvDataService.findById(id);
    }

    /**
     * deletes CircleCsv from database
     *
     * @param record CircleCsv type object
     */
    @Override
    public void delete(CircleCsv record) {
        circleCsvDataService.delete(record);
    }

}
