package org.motechproject.nms.mobileacademy.service.impl;

import org.motechproject.nms.mobileacademy.domain.CourseContentCsv;
import org.motechproject.nms.mobileacademy.repository.CourseContentCsvDataService;
import org.motechproject.nms.mobileacademy.service.CourseContentCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This file contains the implementation for CourseContentCsvService. It will be
 * used to operate on CourseContentCsv Tables
 */
@Service("CourseContentCsvService")
public class CourseContentCsvServiceImpl implements CourseContentCsvService {

    @Autowired
    private CourseContentCsvDataService courseContentCsvDataService;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CourseContentCsvService#delete
     * (org.motechproject.nms.mobileacademy.domain.CourseContentCsv)
     * 
     * to delete the a CourseContentCsv record
     */
    @Override
    public void delete(CourseContentCsv courseContentCsv) {
        courseContentCsvDataService.delete(courseContentCsv);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.motechproject.nms.mobileacademy.service.CourseContentCsvService#deleteAll
     * ()
     * 
     * to delete all the CourseContentCsv records existing in the system
     */
    @Override
    public void deleteAll() {
        courseContentCsvDataService.deleteAll();
    }
}
