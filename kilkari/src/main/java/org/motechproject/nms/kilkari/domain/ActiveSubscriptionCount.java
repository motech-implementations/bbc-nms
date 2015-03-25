package org.motechproject.nms.kilkari.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This entity is used to store a single record that represents
 * the count of Active/PendingActivation subscriptions in the system.
 * The count shall be incremented in the entity when a new subscription
 * is created. It shall be decremented when a subscription is deactivated
 * or completed.
 */
@Entity
public class ActiveSubscriptionCount {

    @Unique
    @Field(required = true)
    @Min(1)
    @Max(1)
    @NotNull
    private Long index;
    
    @Field(required = true)
    private Long count;

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getActiveUserCount() {
        return count;
    }

    public void setActiveUserCount(Long activeUserCount) {
        this.count = activeUserCount;
    }

}
