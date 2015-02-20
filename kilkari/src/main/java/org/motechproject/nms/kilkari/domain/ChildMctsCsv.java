package org.motechproject.nms.kilkari.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class ChildMctsCsv {

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
    private String motherName;

    @Field
    private String motherId;

    @Field
    private String whomPhoneNo;

    @Field
    private String birthdate;

    @Field
    private String entryType;

    public String getStateId() {
        return stateId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public String getWhomPhoneNo() {
        return whomPhoneNo;
    }

    public void setWhomPhoneNo(String whomPhoneNo) {
        this.whomPhoneNo = whomPhoneNo;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

}
