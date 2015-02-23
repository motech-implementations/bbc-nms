package org.motechproject.nms.kilkari.service;

import java.util.List;

import org.motechproject.nms.kilkari.domain.SubscriptionPack;

public interface SubscriptionPackService {
    
    void create(String name, String message);

    void add(SubscriptionPack record);

    SubscriptionPack findRecordByName(String recordName);

    List<SubscriptionPack> getRecords();

    void delete(SubscriptionPack record);

    void update(SubscriptionPack record);
}
