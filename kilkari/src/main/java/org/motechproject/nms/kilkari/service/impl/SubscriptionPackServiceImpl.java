package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.kilkari.repository.SubscriptionPackDataService;
import org.motechproject.nms.kilkari.service.SubscriptionPackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("subscriptionPackService")
public class SubscriptionPackServiceImpl implements SubscriptionPackService {

	@Autowired
	private SubscriptionPackDataService subscriptionPackDataService; 
	
	@Override
	public void create(String name, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(SubscriptionPack record) {
		// TODO Auto-generated method stub

	}

	@Override
	public SubscriptionPack findRecordByName(String recordName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubscriptionPack> getRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(SubscriptionPack record) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(SubscriptionPack record) {
		// TODO Auto-generated method stub

	}

}