package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.repository.ChildMctsCsvDataService;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on ChildMctsCsv
 */

@Service("childMctsCsvService")
public class ChildMctsCsvServiceImpl implements ChildMctsCsvService {

    @Autowired
    private ChildMctsCsvDataService childMctsCsvDataService;

    /**
     * deletes ChildMctsCsv from database
     *
     * @param record ChildMctsCsv type object
     */
    @Override
    public void delete(ChildMctsCsv record) {
        childMctsCsvDataService.delete(record);

    }

    /**
     * gets ChildMctsCsv object based by id
     *
     * @param id primary key of the record
     * @return ChildMctsCsv type object
     */
    @Override
    public ChildMctsCsv findRecordById(Long id) {
        return childMctsCsvDataService.findById(id);
    }

}
