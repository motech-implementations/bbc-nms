package org.motechproject.nms.kilkari.domain;

import org.motechproject.nms.kilkari.commons.Constants;

/**
 * This enum define the subscription pack types
 * their duration and start week.
 */
public enum SubscriptionPack  {
    
    PACK_72_WEEKS("pack_72_weeks"),
    PACK_48_WEEKS("pack_48_weeks");

    private String value;

    SubscriptionPack(String value) {
        this.value = value;
    }

    public Integer getStartWeekNumber() {
        return PACK_72_WEEKS.equals(this)? Constants.START_WEEK_OF_72_WEEK_PACK:Constants.START_WEEK_OF_48_WEEK_PACK;
    }

    public Integer getDuration() {
        return PACK_72_WEEKS.equals(this)?Constants.DURATION_OF_72_WEEK_PACK:Constants.DURATION_OF_48_WEEK_PACK;
    }

    /**
     * find SubscriptionPack object By subscriptionPackName
     *
     * @param subscriptionPackName name of the content type i.e Content, Prompt
     * @return ContentType object return and it can be null also
     */
    public static SubscriptionPack findByName(final String subscriptionPackName) {
        SubscriptionPack subscriptionPackReturn = null;
        for (SubscriptionPack subscriptionPack : SubscriptionPack.values()) {
            if (subscriptionPack.value.equalsIgnoreCase(subscriptionPackName)) {
                subscriptionPackReturn = subscriptionPack;
                break;
            }
        }
        return subscriptionPackReturn;
    }
}
