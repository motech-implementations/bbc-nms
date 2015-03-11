package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.Subscriber;

public interface SubscriberService {

    void update(Subscriber record);
    
    Subscriber create(Subscriber subscriber);

    void deleteAll();
    
}
