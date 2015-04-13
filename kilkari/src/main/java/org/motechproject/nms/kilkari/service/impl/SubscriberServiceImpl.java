package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.BeneficiaryType;
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
     * This method gets subscriber by msisdn, childMctsId, motherMctsId, stateCode, beneficiaryType
     * @param msisdn String type object
     * @param motherMctsId String type object
     * @param childMctsId String type object
     * @param stateCode String type object
     * @param beneficiaryType BeneficiaryType type object
     * @return Subscriber type object
     */
    @Override
    public Subscriber getSubscriberByMsisdnMotherMctsIdChildMctsIdStateCodeAndBeneficiaryType(String msisdn, String motherMctsId, String childMctsId, Long stateCode, BeneficiaryType beneficiaryType) {
        return subscriberDataService.findRecordByMsisdnMotherMctsIdChildMctsIdStateCodeAndBeneficiaryType(msisdn, motherMctsId, childMctsId, stateCode, beneficiaryType);
    }
    
    /**
     * Deletes specified subscriber record from database
     * @param subscriber Subscriber type object
     */
    @Override
    public void delete(Subscriber subscriber) {
        subscriberDataService.delete(subscriber);
    }


    /**
     * This method gets subscriber by childMctsId and stateCode
     * @param childMctsId String type object
     * @param stateCode String type object
     * @return Subscriber type object
     */
    @Override
    public Subscriber getSubscriberByChildMctsId(String childMctsId, Long stateCode) {
        return subscriberDataService.findRecordByChildMctsId(childMctsId, stateCode);
    }

    /**
     * This method gets subscriber by motherMctsId
     * @param motherMctsId String type object
     * @param stateCode String type object
     * @return Subscriber type object
     */
    @Override
    public Subscriber getSubscriberByMotherMctsId(String motherMctsId, Long stateCode) {
        return subscriberDataService.findRecordByMotherMctsId(motherMctsId, stateCode);
    }

}
