package org.motechproject.nms.kilkari.service;

import java.util.List;

import org.motechproject.nms.kilkari.domain.ChildMctsCsv;

public interface ChildMctsCsvService {

    void processChildMctsCsv(String csvFileName, List<Long> uploadedIDs);
    
}
