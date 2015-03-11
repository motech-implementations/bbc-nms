package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.repository.MotherMctsCsvDataService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("motherMctsCsvService")
public class MotherMctsCsvServiceImpl implements MotherMctsCsvService {

    @Autowired
    private MotherMctsCsvDataService motherMctsCsvDataService;

    @Override
    public void delete(MotherMctsCsv record) {
        motherMctsCsvDataService.delete(record);

    }

    @Override
    public MotherMctsCsv findRecordById(Long id) {
        return motherMctsCsvDataService.findById(id);
    }

}
