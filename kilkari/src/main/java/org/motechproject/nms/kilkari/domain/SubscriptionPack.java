package org.motechproject.nms.kilkari.domain;

import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Unique;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
@Entity
public class SubscriptionPack {
	
	@Unique
	@Field(required=true)
	private String name;
	
	@Unique
	@Persistent(mappedBy = "subscriptionPack")	
	private int startWeekNumber;
	
	@Field(required=true)
	private int durationInWeeks;
	
	@Field(required=true)
	private int numMsgPerWeek;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStartWeekNumber() {
		return startWeekNumber;
	}

	public void setStartWeekNumber(int startWeekNumber) {
		this.startWeekNumber = startWeekNumber;
	}

	public int getDurationInWeeks() {
		return durationInWeeks;
	}

	public void setDurationInWeeks(int durationInWeeks) {
		this.durationInWeeks = durationInWeeks;
	}

	public int getNumMsgPerWeek() {
		return numMsgPerWeek;
	}

	public void setNumMsgPerWeek(int numMsgPerWeek) {
		this.numMsgPerWeek = numMsgPerWeek;
	}
	
}
