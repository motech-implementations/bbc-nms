package org.motechproject.nms.kilkari.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Unique;

/**
 * This entity represents the subscription pack record.
 */
@Entity(recordHistory=true)
public class SubscriptionPack extends MdsEntity  {
    
    @Unique
    @Field(required = true)
    private String name;
    
    @Unique
    private Integer startWeekNumber;
    
    @Field(required = true)
    private Integer durationInWeeks;
    
    @Field(required = true)
    private Integer numMsgPerWeek;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStartWeekNumber() {
        return startWeekNumber;
    }

    public void setStartWeekNumber(Integer startWeekNumber) {
        this.startWeekNumber = startWeekNumber;
    }

    public Integer getDurationInWeeks() {
        return durationInWeeks;
    }

    public void setDurationInWeeks(Integer durationInWeeks) {
        this.durationInWeeks = durationInWeeks;
    }

    public Integer getNumMsgPerWeek() {
        return numMsgPerWeek;
    }

    public void setNumMsgPerWeek(Integer numMsgPerWeek) {
        this.numMsgPerWeek = numMsgPerWeek;
    }
    
}
