package org.motechproject.nms.mobilekunji.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Persistent;
import java.util.Set;

/**
 * Created by abhishek on 18/3/15.
 */

@Entity(recordHistory = true)
public class ServiceConsumptionCall extends MdsEntity {

    @Field
    private Long callId;

    @Field
    private Long nmsFlwId;

    @Field
    private String circle;

    @Field
    private DateTime callStartTime;

    @Field
    private DateTime callEndTime;

    @Field
    @Persistent(defaultFetchGroup = "true")
    private Set<CardContent> cardContent;

    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
    }

    public Long getNmsFlwId() {
        return nmsFlwId;
    }

    public void setNmsFlwId(Long nmsFlwId) {
        this.nmsFlwId = nmsFlwId;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public DateTime getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(DateTime callStartTime) {
        this.callStartTime = callStartTime;
    }

    public DateTime getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(DateTime callEndTime) {
        this.callEndTime = callEndTime;
    }

    public Set<CardContent> getCardContent() {
        return cardContent;
    }

    public void setCardContent(Set<CardContent> cardContent) {
        this.cardContent = cardContent;
    }

    @Override
    public String toString() {
        return "ServiceConsumptionCall{" +
                "callId=" + callId +
                ", nmsFlwId=" + nmsFlwId +
                ", circle='" + circle + '\'' +
                ", callStartTime=" + callStartTime +
                ", callEndTime=" + callEndTime +
                ", cardContent=" + cardContent +
                '}';
    }
}
