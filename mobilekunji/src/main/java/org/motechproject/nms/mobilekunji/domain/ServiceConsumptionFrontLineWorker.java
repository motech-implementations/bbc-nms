package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.domain.MdsEntity;

/**
 * Created by abhishek on 18/3/15.
 */

@Entity(recordHistory = true)
public class ServiceConsumptionFrontLineWorker  extends MdsEntity {

    private Long nmsFlwId;

    private Integer endofUsagePromt;

    private Boolean welcomePromptFlag;

    private Integer currentUsageInPulses;

    public Long getNmsFlwId() {
        return nmsFlwId;
    }

    public void setNmsFlwId(Long nmsFlwId) {
        this.nmsFlwId = nmsFlwId;
    }

    public Integer getEndofUsagePromt() {
        return endofUsagePromt;
    }

    public void setEndofUsagePromt(Integer endofUsagePromt) {
        this.endofUsagePromt = endofUsagePromt;
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
        return "ServiceConsumptionFrontLineWorker{" +
                "nmsFlwId=" + nmsFlwId +
                ", endofUsagePromt=" + endofUsagePromt +
                ", welcomePromptFlag=" + welcomePromptFlag +
                ", currentUsageInPulses=" + currentUsageInPulses +
                '}';
    }
}
