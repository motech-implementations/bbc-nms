package org.motechproject.nms.kilkari.service;

import java.util.List;

import org.motechproject.nms.kilkari.domain.MotherMctsCsv;

public interface MotherMctsCsvService {
	
	void create(String name, String message);

    void add(MotherMctsCsv record);

    MotherMctsCsv findRecordByName(String recordName);

    List<MotherMctsCsv> getRecords();

    void delete(MotherMctsCsv record);

    void update(MotherMctsCsv record);
}
