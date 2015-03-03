package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.repository.SubscriberDataService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used to create,delete and get Subscriber object
 */
@Service("subscriberService")
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberDataService subscriberDataService;

    /**
     * Updates Subscriber record
     * @param record Subscriber type object
     */
    @Override
    public void update(Subscriber record) {
        subscriberDataService.update(record);

    }

    /**
     * This method creates subscriber
     * @param subscriber Subscriber type object
     * @return Subscriber type object
     */
    @Override
    public Subscriber create(Subscriber subscriber) {
        return subscriberDataService.create(subscriber);
    }

    /**
     * Deletes all subscribers from database
     */
    @Override
    public void deleteAll() {
        subscriberDataService.deleteAll();

    }

    /**
     * This method gets subscriber by msisdn
     * @param msisdn String type object
     * @return Subscriber type object
     */
    @Override
    public Subscriber getSubscriberByMsisdn(String msisdn) {
        return subscriberDataService.findRecordByMsisdn(msisdn);
    }
    
    /**
     * This method gets subscriber by msisdn and childMctsId
     * @param msisdn String type object
     * @param childMctsId String type object
     * @return Subscriber type object
     */
    @Override
    public Subscriber getSubscriberByMsisdnAndChildMctsId(String msisdn, String childMctsId, Long stateCode) {
        return subscriberDataService.findRecordByMsisdnAndChildMctsId(msisdn, childMctsId, stateCode);
    }
    
    /**
     * This method gets subscriber by msisdn and motherMctsId
     * @param msisdn String type object
     * @param motherMctsId String type object
     * @return Subscriber type object
     */
    @Override
    public Subscriber getSubscriberByMsisdnAndMotherMctsId(String msisdn, String motherMctsId, Long stateCode) {
        return subscriberDataService.findRecordByMsisdnAndMotherMctsId(msisdn, motherMctsId, stateCode);
    }
    
    /**
     * Deletes specified subscriber record from database
     * @param subscriber Subscriber type object
     */
    @Override
    public void delete(Subscriber subscriber) {
        subscriberDataService.delete(subscriber);
    }
    


}
