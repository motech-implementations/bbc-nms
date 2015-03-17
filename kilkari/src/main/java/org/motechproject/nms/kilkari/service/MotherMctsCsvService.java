package org.motechproject.nms.kilkari.service;

import java.util.List;

import org.motechproject.nms.kilkari.domain.MotherMctsCsv;

public interface MotherMctsCsvService {
    

    void processMotherMctsCsv(String csvFileName, List<Long> uploadedIDs);

}
