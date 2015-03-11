package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.repository.ChildMctsCsvDataService;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("childMctsCsvService")
public class ChildMctsCsvServiceImpl implements ChildMctsCsvService {

    @Autowired
    private ChildMctsCsvDataService childMctsCsvDataService;
    
    @Override
    public void delete(ChildMctsCsv record) {
        childMctsCsvDataService.delete(record);

    }

    @Override
    public ChildMctsCsv findRecordById(Long id) {
        return childMctsCsvDataService.findById(id);
    }

}
