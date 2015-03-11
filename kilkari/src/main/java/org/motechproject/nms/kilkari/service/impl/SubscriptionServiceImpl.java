package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import javax.jdo.Query;

import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.nms.kilkari.domain.Status;
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
    public void deleteAll() {
        subscriptionDataService.deleteAll();

    }

    @Override
    public void update(Subscription record) {
        subscriptionDataService.update(record);

    }
    
    public Subscription findRecordIsDeactivatedBySystem(boolean isDeactivatedBySystem) {
        return subscriptionDataService.findRecordIsDeactivatedBySystem(isDeactivatedBySystem);
    }
    
    public Subscription getActiveSubscriptionByMsisdnPack(String msisdn, String packName) {
        Subscription subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.ACTIVE);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.PENDING_ACTIVATION);
        }
        return subscription;
    }

    public Subscription getActiveSubscriptionByMctsIdPack(String mctsId, String packName, Long stateCode) {
        Subscription subscription = subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.ACTIVE, stateCode);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.PENDING_ACTIVATION, stateCode);
        }
        return subscription;
    }

    @Override
    public Subscription create(Subscription subscription) {
        return subscriptionDataService.create(subscription); 
    }

    @Override
    public long getActiveUserCount() {
        
        List<Subscription> activeRecord = subscriptionDataService.getSubscriptionByStatus(Status.ACTIVE);
        List<Subscription> pendingRecord = subscriptionDataService.getSubscriptionByStatus(Status.PENDING_ACTIVATION);
        return activeRecord.size() + pendingRecord.size();
    }
    
    public class LlcListQueryExecutionImpl implements
    QueryExecution<Integer> {

        @Override
        public Integer execute(Query query,
                InstanceSecurityRestriction restriction) {
            query.setResult("COUNT() ");
            return (Integer) query.execute();
        }
    }
    
    public Subscription getSubscriptionByMctsIdState(String mctsId, Long stateCode){
        return subscriptionDataService.getSubscriptionByMctsIdState(mctsId, stateCode);
    }
}
