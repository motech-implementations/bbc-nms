package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.repository.SubscriptionDataService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("subscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {

	@Autowired
    private SubscriptionDataService subscriptionDataService;
	
	@Override
	public void create(String name, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void add(Subscription record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Subscription findRecordByName(String recordName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Subscription> getRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Subscription record) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Subscription record) {
		// TODO Auto-generated method stub

	}
	
	public Subscription findRecordIsDeactivatedBySystem(boolean isDeactivatedBySystem){
		return subscriptionDataService.findRecordIsDeactivatedBySystem(isDeactivatedBySystem);
	}
	
    public Subscription getSubscriptionByMsisdnPackStatus(String msisdn, String packName, Status status){
        return subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, status);
    }

    public Subscription getPackSubscriptionByMctsIdPackStatus(String mctsId, String packName, Status status){
        return subscriptionDataService.getSubscriptionByMsisdnPackStatus(mctsId, packName, status);
    }

    @Override
    public Subscription create(Subscription subscription) {
        return subscriptionDataService.create(subscription); 
    }
}
