package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * Created by abhishek on 18/3/15.
 */

@Entity(recordHistory = true)
public class ServiceConsumptionFlw extends MdsEntity {

    @Field
    private Long nmsFlwId;

    @Field
    private Integer endOfUsagePrompt;

    @Field
    private Boolean welcomePromptFlag;

    @Field
    private Integer currentUsageInPulses;

    @Field
    private Integer msisdn;

    public Long getNmsFlwId() {
        return nmsFlwId;
    }

    public void setNmsFlwId(Long nmsFlwId) {
        this.nmsFlwId = nmsFlwId;
    }

    public Integer getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(Integer msisdn) {
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
        return "ServiceConsumptionFlw{" +
                "nmsFlwId=" + nmsFlwId +
                ", endOfUsagePrompt=" + endOfUsagePrompt +
                ", welcomePromptFlag=" + welcomePromptFlag +
                ", currentUsageInPulses=" + currentUsageInPulses +
                ", msisdn=" + msisdn +
                '}';
    }
}
