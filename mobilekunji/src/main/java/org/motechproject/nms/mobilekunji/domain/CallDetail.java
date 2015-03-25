package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Persistent;
import java.util.Set;

/**
 * This class Models data records provided in the Service Consumption Call
 */

@Entity(recordHistory = true)
public class CallDetail extends MdsEntity {

    @Field
    private String callId;

    @Field
    private Long nmsFlwId;

    @Field
    private String circle;

    @Field
    private Integer callStartTime;

    @Field
    private Integer callEndTime;

    @Field
    @Persistent(defaultFetchGroup = "true")
    private Set<CardContent> cardContent;

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
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

    public Integer getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(Integer callStartTime) {
        this.callStartTime = callStartTime;
    }

    public Integer getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(Integer callEndTime) {
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
        return "CallDetail{" +
                "callId=" + callId +
                ", nmsFlwId=" + nmsFlwId +
                ", circle='" + circle + '\'' +
                ", callStartTime=" + callStartTime +
                ", callEndTime=" + callEndTime +
                ", cardContent=" + cardContent +
                '}';
    }
}
