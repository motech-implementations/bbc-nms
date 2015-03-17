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

    @Override
    public void delete(CourseContentCsv courseContentCsv) {
        courseContentCsvDataService.delete(courseContentCsv);
    }

    @Override
    public void deleteAll() {
        courseContentCsvDataService.deleteAll();
    }

    @Override
    public CourseContentCsv findById(Long id) {
        CourseContentCsv courseContentCsv = courseContentCsvDataService
                .findById(id);
        return courseContentCsv;
    }
}
