package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.BeneficiaryType;
import org.motechproject.nms.kilkari.domain.Subscriber;

/**
 * This interface provides methods to create,delete and get Subscriber object
 */
public interface SubscriberService {

    /**
     * Updates Subscriber record
     * @param record Subscriber type object
     */
    void update(Subscriber record);

    /**
     * This method creates subscriber
     * @param subscriber Subscriber type object
     * @return Subscriber type object
     */
    Subscriber create(Subscriber subscriber);

    /**
     * Deletes all subscribers from database
     */
    void deleteAll();

    /**
     * This method gets subscriber by msisdn
     * @param msisdn String type object
     * @return Subscriber type object
     */
    Subscriber getSubscriberByMsisdn(String msisdn);

    /**
     * This method gets subscriber by msisdn, childMctsId, motherMctsId, stateCode, beneficiaryType
     * @param msisdn String type object
     * @param motherMctsId String type object
     * @param childMctsId String type object
     * @param stateCode String type object
     * @param beneficiaryType BeneficiaryType type object
     * @return Subscriber type object
     */
    Subscriber getSubscriberByMsisdnMotherMctsIdChildMctsIdStateCodeAndBeneficiaryType(String msisdn, String motherMctsId, String childMctsId, Long stateCode, BeneficiaryType beneficiaryType);

    /**
     * Deletes specified subscriber record from database
     * @param subscriber Subscriber type object
     */
    void delete(Subscriber subscriber);

    /**
     * This method gets subscriber by childMctsId and stateCode
     * @param childMctsId String type object
     * @param stateCode String type object
     * @return Subscriber type object
     */
    Subscriber getSubscriberByChildMctsId(String childMctsId, Long stateCode);

    /**
     * This method gets subscriber by motherMctsId and stateCode
     * @param motherMctsId String type object
     * @param stateCode String type object
     * @return Subscriber type object
     */
    Subscriber getSubscriberByMotherMctsId(String motherMctsId, Long stateCode);
}
