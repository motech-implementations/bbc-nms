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
    
}
