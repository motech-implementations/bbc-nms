package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import javax.jdo.Query;

import org.motechproject.mds.filter.Filter;
import org.motechproject.mds.filter.FilterType;
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
    public void create(String name, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void add(Subscription record) {
        subscriptionDataService.create(record);

    }

    @Override
    public Subscription findRecordByName(String recordName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Subscription> getRecords() {
        subscriptionDataService.retrieveAll();
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(Subscription record) {
        subscriptionDataService.delete(record);

    }
    
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
        Subscription subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.Active);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.PendingActivation);
        }
        return subscription;
    }

    public Subscription getActiveSubscriptionByMctsIdPack(String mctsId, String packName, Long stateCode) {
        Subscription subscription = subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.Active, stateCode);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMctsIdPackStatus(mctsId, packName, Status.PendingActivation, stateCode);
        }
        return subscription;
    }

    @Override
    public Subscription create(Subscription subscription) {
        return subscriptionDataService.create(subscription); 
    }

    @Override
    public long getActiveUserCount() {
        
        List<Subscription> activeRecord = subscriptionDataService.getSubscriptionByStatus(Status.Active);
        List<Subscription> pendingRecord = subscriptionDataService.getSubscriptionByStatus(Status.PendingActivation);
        return activeRecord.size() + pendingRecord.size();
        
        
        //Filter filter = new Filter();
        //filter.setField("status");
        //filter.setType(FilterType.fromString("Active"));
        //long activeRecord = subscriptionDataService.countForFilter(filter);
        //filter.setType(FilterType.fromString("PendingActivation"));
        //long pendingRecord = subscriptionDataService.countForFilter(filter);
        //return activeRecord + pendingRecord;
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
        Subscription subscription = subscriptionDataService.getSubscriptionByMctsIdState(mctsId, stateCode);
        return subscription;
    }
}
