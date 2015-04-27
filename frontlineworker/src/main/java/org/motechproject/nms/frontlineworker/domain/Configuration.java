package org.motechproject.nms.frontlineworker.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Unique;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Models data for configuration records in a portable manner.
 */
@Entity(recordHistory = true)
public class Configuration extends MdsEntity {

    @Field(required = true)
    @Unique
    @Min(1)
    @Max(1)
    private Long index;

    @Field(required = true)
    public Integer purgeDate;

    @Field(required = true)
    private String deletionEventCronExpression;

    public Configuration() {
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Integer getPurgeDate() {
        return purgeDate;
    }

    public void setPurgeDate(Integer purgeDate) {
        this.purgeDate = purgeDate;
    }

    public String getDeletionEventCronExpression() {
        return deletionEventCronExpression;
    }
    
    public void setDeletionEventCronExpression(String deletionEventCronExpression) {
        this.deletionEventCronExpression = deletionEventCronExpression;
    }
}
