package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.Subscriber;

/**
 * This interface is used for crud operations on Subscriber
 */

public interface SubscriberService {

    /**
     * update Subscriber in database
     *
     * @param record Subscriber type object
     */
    void update(Subscriber record);

    /**
     * create new Subscriber record  in database
     *
     * @param subscriber Subscriber type object
     */
    Subscriber create(Subscriber subscriber);

    /**
     * Delete all subscribers from database
     */
    void deleteAll();
    
}
