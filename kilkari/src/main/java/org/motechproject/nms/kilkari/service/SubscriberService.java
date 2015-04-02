package org.motechproject.nms.kilkari.service;

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
     * This method gets subscriber by msisdn and childMctsId
     * @param msisdn String type object
     * @param childMctsId String type object
     * @return Subscriber type object
     */
    Subscriber getSubscriberByMsisdnAndChildMctsId(String msisdn, String childMctsId, Long stateCode);

    /**
     * This method gets subscriber by msisdn and motherMctsId
     * @param msisdn String type object
     * @param motherMctsId String type object
     * @return Subscriber type object
     */
    Subscriber getSubscriberByMsisdnAndMotherMctsId(String msisdn, String motherMctsId, Long stateCode);

    /**
     * Deletes specified subscriber record from database
     * @param subscriber Subscriber type object
     */
    void delete(Subscriber subscriber);
    
}
