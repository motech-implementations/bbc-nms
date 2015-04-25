package org.motechproject.nms.mobileacademy.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;

/**
 * CallDetail object to refer call related data.
 *
 */
@Entity
public class CallDetail extends MdsEntity {

    @Field(required = true)
    @UIDisplayable(position = 0)
    private Long callId;

    @Field(required = true)
    @UIDisplayable(position = 1)
    private Long flwId;

    @Field(required = true)
    @UIDisplayable(position = 2)
    private Long msisdn;

    @Field(required = true)
    @UIDisplayable(position = 3)
    private String circle;

    @Field(required = true)
    @UIDisplayable(position = 4)
    private String operator;

    @Field(required = true)
    @UIDisplayable(position = 5)
    private DateTime callStartTime;

    @Field(required = true)
    @UIDisplayable(position = 6)
    private DateTime callEndTime;

    @Field(required = true)
    @UIDisplayable(position = 7)
    private Integer callStatus;

    @Field(required = true)
    @UIDisplayable(position = 8)
    private Integer callDisconnectReason;

    public Long getCallId() {
        return callId;
    }

    public void setCallId(Long callId) {
        this.callId = callId;
    }

    public Long getFlwId() {
        return flwId;
    }

    public void setFlwId(Long flwId) {
        this.flwId = flwId;
    }

    public Long getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(Long msisdn) {
        this.msisdn = msisdn;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

    public Integer getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(Integer callStatus) {
        this.callStatus = callStatus;
    }

    public Integer getCallDisconnectReason() {
        return callDisconnectReason;
    }

    public void setCallDisconnectReason(Integer callDisconnectReason) {
        this.callDisconnectReason = callDisconnectReason;
    }

}
