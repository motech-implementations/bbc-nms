package org.motechproject.nms.kilkari.domain;

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
        return PACK_72_WEEKS.equals(this)?1:27;
    }

    public Integer getDuration() {
        return PACK_72_WEEKS.equals(this)?72:48;
    }

    @Override
    public String toString() {
        return name;
    }

}
