package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ChildMctsCsv;

public interface ChildMctsCsvService {

    void delete(ChildMctsCsv record);
    
    ChildMctsCsv findRecordById(Long id);
    
}