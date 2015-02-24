package org.motechproject.nms.kilkari.domain;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Persistent;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This entity represents the subscription record.
 */
@Entity(recordHistory=true)
public class Subscription extends MdsEntity {

    @Persistent
    private SubscriptionPack subscriptionPack;

    @Persistent
    private Subscriber subscriber;

    @Field(required = true)
    private Integer operatorCode;  
    
    @Field(required = true)
    private String msisdn;
    
    @Field
    private String mctsId;
    
    @Field
    private Long stateCode;
    
    @Field
    private String packName;

    @Field(required = true)
    private Channel channel;

    @Field(required = true)
    private Status status;

    @Field
    private Boolean isDeactivatedBySystem;

    @Field(required = true)
    private Integer weekNumber;
    
    @Field(required = true)
    private Integer messageNumber;

    @Field(required = true)
    private DateTime startDate;

    @Field(required = true)
    private DateTime nextObdDate;

    @Field
    @Column(name = "oldSubscritption_Id")
    private Subscription oldSubscritptionId;

    public SubscriptionPack getSubscriptionPack() {
        return subscriptionPack;
    }

    public void setSubscriptionPack(SubscriptionPack subscriptionPack) {
        this.subscriptionPack = subscriptionPack;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Integer getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(Integer operatorCode) {
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

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
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

    public Boolean getIsDeactivatedBySystem() {
        return isDeactivatedBySystem;
    }

    public void setIsDeactivatedBySystem(Boolean isDeactivatedBySystem) {
        this.isDeactivatedBySystem = isDeactivatedBySystem;
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

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getNextObdDate() {
        return nextObdDate;
    }

    public void setNextObdDate(DateTime nextObdDate) {
        this.nextObdDate = nextObdDate;
    }

    public Subscription getOldSubscritptionId() {
        return oldSubscritptionId;
    }

    public void setOldSubscritptionId(Subscription oldSubscritptionId) {
        this.oldSubscritptionId = oldSubscritptionId;
    }
}

