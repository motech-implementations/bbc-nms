package org.motechproject.nms.kilkari.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class SubscriptionMeasure {

    @Field(name = "subscriptionId", required=true)
    private Subscription subscription;
    
    @Field(required=true)
    private Integer weekNumber;
    
    @Field(required=true)
    private Integer messageNumber;
    
    @Field(required = true)
    private Status status;

}
