package org.motechproject.nms.kilkari.domain;

import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Persistent;


import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
@Entity(module="",  name="subscriber")
public class Subscriber {
	
	@Field(required=true)
	private String msisdn;
	
	@Field
	private String oldMsisdn;
	
	@Field
	private String childMctsId;
	
	@Field
	private String motherMctsId;
	
	@Field(required=true)
	private BeneficiaryType beneficiaryType;
	
	@Field
    private String name;
	
	@Field
	private int age;
	
	@Field
	private long stateId;
	
	@Field
	private long districtId;
	
	@Field
	@Column(length=50)
	private String talukaId;
	
	@Field
	private long healthBlockId;
	
	@Field
	private long phcId;
	
	@Field
	private long subCentreId;
	
	@Field
	private long villageId;
	
	@Field(required=true)
	@Column(length=2)
	private int languageLocationCode;
	
	@Field
	@Column(length=12)
	private int aadharNumber;
	
	@Field
	private DateTime lmp;
	
	@Field
	private DateTime dob;
	
	@Field
	private boolean stillBirth;
	
	@Field
	private boolean abortion;
	
	@Field
	private boolean motherDeath;
	
	@Field
	private boolean childDeath;
	
	@Persistent(mappedBy = "subscriber")
	private List<Subscription> subscriber;

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getOldMsisdn() {
		return oldMsisdn;
	}

	public void setOldMsisdn(String oldMsisdn) {
		this.oldMsisdn = oldMsisdn;
	}

	public String getChildMctsId() {
		return childMctsId;
	}

	public void setChildMctsId(String childMctsId) {
		this.childMctsId = childMctsId;
	}

	public String getMotherMctsId() {
		return motherMctsId;
	}

	public void setMotherMctsId(String motherMctsId) {
		this.motherMctsId = motherMctsId;
	}

	public BeneficiaryType getBeneficiaryType() {
		return beneficiaryType;
	}

	public void setBeneficiaryType(BeneficiaryType beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public long getStateId() {
		return stateId;
	}

	public void setStateId(long stateId) {
		this.stateId = stateId;
	}

	public long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(long districtId) {
		this.districtId = districtId;
	}

	public String getTalukaId() {
		return talukaId;
	}

	public void setTalukaId(String talukaId) {
		this.talukaId = talukaId;
	}

	public long getHealthBlockId() {
		return healthBlockId;
	}

	public void setHealthBlockId(long healthBlockId) {
		this.healthBlockId = healthBlockId;
	}

	public long getPhcId() {
		return phcId;
	}

	public void setPhcId(long phcId) {
		this.phcId = phcId;
	}

	public long getSubCentreId() {
		return subCentreId;
	}

	public void setSubCentreId(long subCentreId) {
		this.subCentreId = subCentreId;
	}

	public long getVillageId() {
		return villageId;
	}

	public void setVillageId(long villageId) {
		this.villageId = villageId;
	}

	public int getLanguageLocationCode() {
		return languageLocationCode;
	}

	public void setLanguageLocationCode(int languageLocationCode) {
		this.languageLocationCode = languageLocationCode;
	}

	public int getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(int aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public DateTime getLmp() {
		return lmp;
	}

	public void setLmp(DateTime lmp) {
		this.lmp = lmp;
	}

	public DateTime getDob() {
		return dob;
	}

	public void setDob(DateTime dob) {
		this.dob = dob;
	}

	public boolean isStillBirth() {
		return stillBirth;
	}

	public void setStillBirth(boolean stillBirth) {
		this.stillBirth = stillBirth;
	}

	public boolean isAbortion() {
		return abortion;
	}

	public void setAbortion(boolean abortion) {
		this.abortion = abortion;
	}

	public boolean isMotherDeath() {
		return motherDeath;
	}

	public void setMotherDeath(boolean motherDeath) {
		this.motherDeath = motherDeath;
	}

	public boolean isChildDeath() {
		return childDeath;
	}

	public void setChildDeath(boolean childDeath) {
		this.childDeath = childDeath;
	}
	
	
	
}
