package org.motechproject.nms.kilkari.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

/**
 * This entity records the transactional changes in subscription.
 */
@Entity
public class SubscriptionMeasure {

    @Field(name = "subscriptionId", required=true)
    @UIDisplayable(position = 0)
    private Subscription subscription;
    
    @Field
    @UIDisplayable(position = 2)
    private Integer weekNumber;
    
    @Field
    @UIDisplayable(position = 3)
    private Integer messageNumber;
    
    @Field(required = true)
    @UIDisplayable(position = 1)
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
