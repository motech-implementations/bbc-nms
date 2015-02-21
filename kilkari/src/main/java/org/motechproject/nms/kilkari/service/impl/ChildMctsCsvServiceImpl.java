package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.repository.ChildMctsCsvDataService;
import org.motechproject.nms.kilkari.repository.HelloWorldRecordsDataService;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("childMctsCsvService")
public class ChildMctsCsvServiceImpl implements ChildMctsCsvService {

	@Autowired
    private ChildMctsCsvDataService childMctsCsvDataService;
	
	@Override
	public void create(String name, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(ChildMctsCsv record) {
		// TODO Auto-generated method stub

	}

	@Override
	public ChildMctsCsv findRecordByName(String recordName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ChildMctsCsv> getRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(ChildMctsCsv record) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ChildMctsCsv record) {
		// TODO Auto-generated method stub

	}
	
	@Override
    public ChildMctsCsv findRecordById(Long id) {
        return childMctsCsvDataService.findById(id);
    }

    @Override
    public void deleteAll() {
        childMctsCsvDataService.deleteAll();
    }

    @Override
    public ChildMctsCsv findById(Long id) {
        return childMctsCsvDataService.findById(id);
    }

}
