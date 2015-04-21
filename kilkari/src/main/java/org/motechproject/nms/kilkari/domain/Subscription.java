package org.motechproject.nms.kilkari.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Persistent;

/**
 * This entity represents the subscription record.
 */
@Entity(recordHistory=true)
public class Subscription extends MdsEntity {

    @Field(required = true)
    @UIDisplayable(position = 0)
    private String msisdn;
    
    @Field
    @UIDisplayable(position = 1)
    private String operatorCode;
    
    @Field
    @UIDisplayable(position = 2)
    private Long stateCode;
    
    @Field
    @UIDisplayable(position = 3)
    private String mctsId;
    
    @Field(required = true)
    @UIDisplayable(position = 4)
    private Channel channel;
    
    @Field
    @UIDisplayable(position = 5)
    private SubscriptionPack packName;

    @Field(required = true)
    @UIDisplayable(position = 6)
    private Status status;

    @Field
    @UIDisplayable(position = 7)
    private DeactivationReason deactivationReason;

    @Field
    @UIDisplayable(position = 8)
    private Long startDate;
    
    @Field
    @UIDisplayable(position = 9)
    private Integer weekNumber;
    
    @Field
    @UIDisplayable(position = 10)
    private Integer messageNumber;

    @Field
    @UIDisplayable(position = 11)
    private DateTime lastObdDate;
    
    @Persistent
    @UIDisplayable(position = 12)
    private Subscriber subscriber;

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Long getStateCode() {
        return stateCode;
    }

    public String getMctsId() {
        return mctsId;
    }

    public void setMctsId(String mctsId) {
        this.mctsId = mctsId;
    }

    public void setStateCode(Long stateCode) {
        this.stateCode = stateCode;
    }

    public SubscriptionPack getPackName() {
        return packName;
    }

    public void setPackName(SubscriptionPack packName) {
        this.packName = packName;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    public DeactivationReason getDeactivationReason() {
        return deactivationReason;
    }

    public void setDeactivationReason(DeactivationReason deactivationReason) {
        this.deactivationReason = deactivationReason;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public Integer getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(Integer messageNumber) {
        this.messageNumber = messageNumber;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public DateTime getLastObdDate() {
        return lastObdDate;
    }

    public void setLastObdDate(DateTime lastObdDate) {
        this.lastObdDate = lastObdDate;
    }

}

