package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Persistent;
import java.util.HashSet;
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
    private Long callStartTime;

    @Field
    private Long callEndTime;

    @Field
    @Persistent(defaultFetchGroup = "true")
    private Set<CardContent> cardContent;


    public CallDetail(String callId, Long nmsFlwId, String circle, Long callStartTime, Long callEndTime) {
        this.callId = callId;
        this.nmsFlwId = nmsFlwId;
        this.circle = circle;
        this.callStartTime = callStartTime;
        this.callEndTime = callEndTime;

    }

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

    public Long getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(Long callStartTime) {
        this.callStartTime = callStartTime;
    }

    public Long getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(Long callEndTime) {
        this.callEndTime = callEndTime;
    }

    public Set<CardContent> getCardContent() {

        if (cardContent == null){
            cardContent = new HashSet<CardContent>();
        }
        return this.cardContent;
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
