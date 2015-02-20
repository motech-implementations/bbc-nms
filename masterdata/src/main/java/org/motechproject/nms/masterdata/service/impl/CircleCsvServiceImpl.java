package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.CircleCsv;
import org.motechproject.nms.masterdata.repository.CircleCsvDataService;
import org.motechproject.nms.masterdata.service.CircleCsvService;
import org.springframework.beans.factory.annotation.Autowired;

public class CircleCsvServiceImpl implements CircleCsvService {

    @Autowired
    private CircleCsvDataService circleCsvDataService;

    @Override
    public CircleCsv findById(Long id) {
        return circleCsvDataService.findById(id);
    }

    @Override
    public void delete(CircleCsv record) {
        circleCsvDataService.delete(record);
    }

    @Override
    public void deleteAll() {
        circleCsvDataService.deleteAll();
    }
}
