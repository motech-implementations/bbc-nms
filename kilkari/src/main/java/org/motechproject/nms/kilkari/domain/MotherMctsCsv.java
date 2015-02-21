package org.motechproject.nms.kilkari.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

@Entity
public class MotherMctsCsv extends MdsEntity {
	
	@Field
	private String operation;
	
	@Field
	private String stateId;
	
	@Field
	private String districtId;
	
	@Field
	private String talukaId;
	
	@Field
	private String healthBlockId;
	
	@Field
	private String phcId;
	
	@Field
	private String subCentreId;
	
	@Field
	private String villageId;
	
	@Field
	private String idNo;
	
	@Field
	private String name;
	
	@Field
	private String whomPhoneNo;
	
	@Field
	private String lmpDate;
	
	@Field
	private String abortion;
	
	@Field
	private String outcomeNos;
	
	@Field
	private String age;
	
	@Field
	private String entryType;
	
	@Field
	private String aadharNo;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getStateId() {
		return stateId;
	}

	public void setStateId(String stateId) {
		this.stateId = stateId;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getTalukaId() {
		return talukaId;
	}

	public void setTalukaId(String talukaId) {
		this.talukaId = talukaId;
	}

	public String getHealthBlockId() {
		return healthBlockId;
	}

	public void setHealthBlockId(String healthBlockId) {
		this.healthBlockId = healthBlockId;
	}

	public String getPhcId() {
		return phcId;
	}

	public void setPhcId(String phcId) {
		this.phcId = phcId;
	}

	public String getSubCentreId() {
		return subCentreId;
	}

	public void setSubCentreId(String subCentreId) {
		this.subCentreId = subCentreId;
	}

	public String getVillageId() {
		return villageId;
	}

	public void setVillageId(String villageId) {
		this.villageId = villageId;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWhomPhoneNo() {
		return whomPhoneNo;
	}

	public void setWhomPhoneNo(String whomPhoneNo) {
		this.whomPhoneNo = whomPhoneNo;
	}

	public String getLmpDate() {
		return lmpDate;
	}

	public void setLmpDate(String lmpDate) {
		this.lmpDate = lmpDate;
	}

	public String getAbortion() {
		return abortion;
	}

	public void setAbortion(String abortion) {
		this.abortion = abortion;
	}

	public String getOutcomeNos() {
		return outcomeNos;
	}

	public void setOutcomeNos(String outcomeNos) {
		this.outcomeNos = outcomeNos;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	
}
