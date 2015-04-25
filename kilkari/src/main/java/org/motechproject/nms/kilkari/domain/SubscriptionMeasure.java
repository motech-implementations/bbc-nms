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
    private Long subscriptionId;
    
    @Field
    @UIDisplayable(position = 2)
    private Integer weekNumber;
    
    @Field
    @UIDisplayable(position = 3)
    private Integer messageNumber;
    
    @Field(required = true)
    @UIDisplayable(position = 1)
    private Status status;

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
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
