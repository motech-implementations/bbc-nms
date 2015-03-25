package org.motechproject.nms.mobilekunji.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data records provided in the FlwDetails
 */

@Entity(recordHistory = true)
public class FlwDetail extends MdsEntity {

    @Field
    private Long nmsFlwId;

    @Field
    private Integer endOfUsagePrompt;

    @Field
    private Integer welcomePromptFlagCounter;

    @Field
    private Integer currentUsageInPulses;

    @Field
    private String msisdn;

    @Field
    private DateTime lastAccessDate;

    public Integer getWelcomePromptFlagCounter() {
        return welcomePromptFlagCounter;
    }

    public void setWelcomePromptFlagCounter(Integer welcomePromptFlagCounter) {
        this.welcomePromptFlagCounter = welcomePromptFlagCounter;
    }

    public DateTime getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(DateTime lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public Long getNmsFlwId() {
        return nmsFlwId;
    }

    public void setNmsFlwId(Long nmsFlwId) {
        this.nmsFlwId = nmsFlwId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Integer getEndOfUsagePrompt() {
        return endOfUsagePrompt;
    }

    public void setEndOfUsagePrompt(Integer endOfUsagePrompt) {
        this.endOfUsagePrompt = endOfUsagePrompt;
    }


    public Integer getCurrentUsageInPulses() {
        return currentUsageInPulses;
    }

    public void setCurrentUsageInPulses(Integer currentUsageInPulses) {
        this.currentUsageInPulses = currentUsageInPulses;
    }

    @Override
    public String toString() {
        return "FlwDetail{" +
                "nmsFlwId=" + nmsFlwId +
                ", endOfUsagePrompt=" + endOfUsagePrompt +
                ", welcomePromptFlagCounter=" + welcomePromptFlagCounter +
                ", currentUsageInPulses=" + currentUsageInPulses +
                ", msisdn='" + msisdn + '\'' +
                ", lastAccessDate=" + lastAccessDate +
                '}';
    }
}
