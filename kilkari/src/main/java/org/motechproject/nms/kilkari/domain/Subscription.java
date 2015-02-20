package org.motechproject.nms.kilkari.domain;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Persistent;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

public class Subscription extends MdsEntity {

	
	@Persistent
	private SubscriptionPack subscriptionPack;

	@Persistent
	private Subscriber subscriber;

	@Field(required=true)
	private Integer operator_Id;

	@Field(required=true)
	private Channel channel;

	@Field(required=true)
	private Status status;

	@Field
	private Boolean isDeactivatedBySystem;

	@Field(required=true)
	private Integer weekNumber;
	
	@Field(required=true)
	private Integer messageNumber;

	@Field(required=true)
	private DateTime startDate;

	@Field(required=true)
	private DateTime nextObdDate;

	@Field
	@Column(name="oldSubscritption_Id")
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

	public Integer getOperator_Id() {
		return operator_Id;
	}

	public void setOperator_Id(Integer operator_Id) {
		this.operator_Id = operator_Id;
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

	public Boolean isDeactivatedBySystem() {
		return isDeactivatedBySystem;
	}

	public void setDeactivatedBySystem(Boolean isDeactivatedBySystem) {
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
