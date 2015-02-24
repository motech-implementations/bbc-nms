package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.repository.MotherMctsCsvDataService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on MotherMctsCsv
 */

@Service("motherMctsCsvService")
public class MotherMctsCsvServiceImpl implements MotherMctsCsvService {

    @Autowired
    private MotherMctsCsvDataService motherMctsCsvDataService;

    /**
     * deletes MotherMctsCsv from database
     *
     * @param record MotherMctsCsv type object
     */
    @Override
    public void delete(MotherMctsCsv record) {
        motherMctsCsvDataService.delete(record);

    }

    /**
     * gets MotherMctsCsv object based by id
     *
     * @param id primary key of the record
     * @return MotherMctsCsv type object
     */
    @Override
    public MotherMctsCsv findRecordById(Long id) {
        return motherMctsCsvDataService.findById(id);
    }

}
