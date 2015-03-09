package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.MotherMctsCsv;

public interface MotherMctsCsvService {
    

    MotherMctsCsv findRecordById(Long id);

    void delete(MotherMctsCsv record);
    
    void deleteAll();
    
    MotherMctsCsv findById(Long id);
}
