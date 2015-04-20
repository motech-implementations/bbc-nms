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

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Integer getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(Integer messageNumber) {
        this.messageNumber = messageNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
}
