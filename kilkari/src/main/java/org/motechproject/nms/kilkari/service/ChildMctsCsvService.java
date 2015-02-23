package org.motechproject.nms.kilkari.service;

import java.util.List;

import org.motechproject.nms.kilkari.domain.ChildMctsCsv;

public interface ChildMctsCsvService {

    void create(String name, String message);

    void add(ChildMctsCsv record);

    ChildMctsCsv findRecordByName(String recordName);

    List<ChildMctsCsv> getRecords();

    void delete(ChildMctsCsv record);

    void update(ChildMctsCsv record);
    
    ChildMctsCsv findRecordById(Long id);
    
    void deleteAll();
    
    ChildMctsCsv findById(Long id);
    
    
}
