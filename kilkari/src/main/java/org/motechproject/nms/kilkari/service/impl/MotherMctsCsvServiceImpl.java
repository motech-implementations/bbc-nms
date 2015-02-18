package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.repository.MotherMctsCsvDataService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("motherMctsCsvService")
public class MotherMctsCsvServiceImpl implements MotherMctsCsvService{

	@Autowired
	private MotherMctsCsvDataService motherMctsCsvDataService;
	
	@Override
	public void create(String name, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(MotherMctsCsv record) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MotherMctsCsv findRecordByName(String recordName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MotherMctsCsv> getRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(MotherMctsCsv record) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(MotherMctsCsv record) {
		// TODO Auto-generated method stub
		
	}

}
