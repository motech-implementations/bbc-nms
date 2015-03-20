package org.motechproject.nms.kilkari.domain;

import org.motechproject.nms.kilkari.commons.Constants;

/**
 * This enum define the subscription pack types
 * their duration and start week.
 */
public enum SubscriptionPack  {
    
    PACK_72_WEEKS("72WeeksPack"),
    PACK_48_WEEKS("48WeeksPack");

    private String name;

    private SubscriptionPack(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStartWeekNumber() {
        return PACK_72_WEEKS.equals(this)? Constants.START_WEEK_OF_72_WEEK_PACK:Constants.START_WEEK_OF_48_WEEK_PACK;
    }

    public Integer getDuration() {
        return PACK_72_WEEKS.equals(this)?Constants.DURATION_OF_72_WEEK_PACK:Constants.DURATION_OF_48_WEEK_PACK;
    }

    @Override
    public String toString() {
        return name;
    }

}
