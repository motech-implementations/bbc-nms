package org.motechproject.nms.mobilekunji.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data records provided in the Service Consumption FLW
 */

@Entity(recordHistory = true)
public class FlwDetail extends MdsEntity {

    @Field
    private Long nmsFlwId;

    @Field
    private Integer endOfUsagePrompt;

    @Field
    private Boolean welcomePromptFlag;

    @Field
    private Integer currentUsageInPulses;

    @Field
    private String msisdn;

    @Field
    private DateTime lastAccessDate;

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

    public Boolean getWelcomePromptFlag() {
        return welcomePromptFlag;
    }

    public void setWelcomePromptFlag(Boolean welcomePromptFlag) {
        this.welcomePromptFlag = welcomePromptFlag;
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
                ", welcomePromptFlag=" + welcomePromptFlag +
                ", currentUsageInPulses=" + currentUsageInPulses +
                ", msisdn=" + msisdn +
                '}';
    }
}
