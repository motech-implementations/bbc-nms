package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
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
    @UIDisplayable(position = 0)
    private String callId;

    @Field
    @UIDisplayable(position = 1)
    private String callingNumber;

    @Field
    @UIDisplayable(position = 2)
    private String operator;

    @Field
    @UIDisplayable(position = 4)
    private Integer callStatus;

    @Field
    @UIDisplayable(position = 5)
    private String callDisconnectReason;

    @Field
    @UIDisplayable(position = 3)
    private String circleCode;

    @Field
    @UIDisplayable(position = 6)
    private Long startTime;

    @Field
    @UIDisplayable(position = 7)
    private Long endTime;

    @Field
    private Long nmsFlwId;

    @Field
    @Persistent(defaultFetchGroup = "true")
    private Set<CardDetail> cardDetail;

    public CallDetail(String callId, String callingNumber, String operator, Integer callStatus,
                      String callDisconnectReason, String circleCode, Long startTime, Long endTime,
                      Long nmsFlwId) {
        this.callId = callId;
        this.callingNumber = callingNumber;
        this.operator = operator;
        this.callStatus = callStatus;
        this.callDisconnectReason = callDisconnectReason;
        this.circleCode = circleCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.nmsFlwId = nmsFlwId;

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

    public String getCallingNumber() {
        return callingNumber;
    }

    public void setCallingNumber(String callingNumber) {
        this.callingNumber = callingNumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(Integer callStatus) {
        this.callStatus = callStatus;
    }

    public String getCallDisconnectReason() {
        return callDisconnectReason;
    }

    public void setCallDisconnectReason(String callDisconnectReason) {
        this.callDisconnectReason = callDisconnectReason;
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
                ", callingNumber='" + callingNumber + '\'' +
                ", operator='" + operator + '\'' +
                ", callStatus=" + callStatus +
                ", callDisconnectReason='" + callDisconnectReason + '\'' +
                ", circleCode='" + circleCode + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", nmsFlwId=" + nmsFlwId +
                ", cardDetail=" + cardDetail +
                '}';
    }
}
