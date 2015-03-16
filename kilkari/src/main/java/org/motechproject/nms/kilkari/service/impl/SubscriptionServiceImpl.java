package org.motechproject.nms.kilkari.service.impl;

import java.util.List;

import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.repository.SubscriptionDataService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jdo.Query;

@Service("subscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionDataService subscriptionDataService;

    /**
     * Query to find list of Active and Pending subscription packs for given msisdn.
     */
    private class ActiveSubscriptionQuery implements QueryExecution<List<String>> {
        private String msisdn;
        private String resultParamName;

        public ActiveSubscriptionQuery(String msisdn, String resultParamName) {
            this.msisdn = msisdn;
            this.resultParamName = resultParamName;
        }

        @Override
        public List<String> execute(Query query, InstanceSecurityRestriction restriction) {
            query.setFilter("msisdn == '" + msisdn + "'");
            query.setFilter("subscriptionPack == ACTIVE or subscriptionPack == PENDING_ACTIVATION");
            query.setResult(resultParamName);
            return null;
        }
    }

    @Override
    public void deleteAll() {
        subscriptionDataService.deleteAll();
    }

    @Override
    public void update(Subscription record) {
        subscriptionDataService.update(record);
    }
    
    @Override
    public Subscription getActiveSubscriptionByMsisdnPack(String msisdn, String packName) {
        Subscription subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.ACTIVE);
        if(subscription == null) {
            subscription = subscriptionDataService.getSubscriptionByMsisdnPackStatus(msisdn, packName, Status.PENDING_ACTIVATION);
        }
        return subscription;
    }

    @Override
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
    
    @Override
    public Subscription getSubscriptionByMctsIdState(String mctsId, Long stateCode){
        return subscriptionDataService.getSubscriptionByMctsIdState(mctsId, stateCode);
    }

    @Override
    public List<String> getActiveSubscriptionByMsisdn(String msisdn) {
        ActiveSubscriptionQuery query = new ActiveSubscriptionQuery(msisdn, "subscriptionPack");
        List<String> subscriptionPackList = subscriptionDataService.executeQuery(query);
        return subscriptionPackList;
    }
}
