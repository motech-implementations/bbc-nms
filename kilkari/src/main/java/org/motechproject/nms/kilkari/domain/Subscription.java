package org.motechproject.nms.kilkari.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Persistent;

/**
 * This entity represents the subscription record.
 */
@Entity(recordHistory=true)
public class Subscription extends MdsEntity {

    @Persistent
    private Subscriber subscriber;

    @Field
    private String operatorCode;
    
    @Field(required = true)
    private String msisdn;
    
    @Field
    private String mctsId;
    
    @Field
    private Long stateCode;
    
    @Field
    private SubscriptionPack packName;

    @Field(required = true)
    private Channel channel;

    @Field(required = true)
    private Status status;

    @Field
    private DeactivationReason deactivationReason;

    @Field
    private Integer weekNumber;
    
    @Field
    private Integer messageNumber;

    @Field
    private Long startDate;

    @Field
    private DateTime nextObdDate;

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

    public DateTime getNextObdDate() {
        return nextObdDate;
    }

    public void setNextObdDate(DateTime nextObdDate) {
        this.nextObdDate = nextObdDate;
    }

}

