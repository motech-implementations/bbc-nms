package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Persistent;
import java.util.HashSet;
import java.util.Set;

/**
 * This class Models data records provided in the CallDetail
 */

@Entity
public class CallDetail extends MdsEntity {

    @Field
    private String callId;

    @Field
    private Long nmsFlwId;

    @Field
    private String circleCode;

    @Field
    private Long startTime;

    @Field
    private Long endTime;

    @Field
    @Persistent(defaultFetchGroup = "true")
    private Set<CardDetail> cardDetail;

    public CallDetail(String callId, Long nmsFlwId, String circleCode, Long startTime, Long endTime) {
        this.callId = callId;
        this.nmsFlwId = nmsFlwId;
        this.circleCode = circleCode;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public String getCircleCode() {
        return circleCode;
    }

    public void setCircleCode(String circleCode) {
        this.circleCode = circleCode;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Set<CardDetail> getCardDetail() {

        if (cardDetail == null) {
            cardDetail = new HashSet<CardDetail>();
        }
        return this.cardDetail;
    }

    public void setCardDetail(Set<CardDetail> cardContent) {
        this.cardDetail = cardContent;
    }

    @Override
    public String toString() {
        return "CallDetail{" +
                "callId='" + callId + '\'' +
                ", nmsFlwId=" + nmsFlwId +
                ", circleCode='" + circleCode + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", cardContent=" + cardDetail +
                '}';
    }
}
