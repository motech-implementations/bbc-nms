package org.motechproject.nms.flw.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Unique;

/**
 * Created by abhishek on 26/1/15.
 */

@Entity
@Unique(name="FLW_COMPOSITE_KEYS", members={"flwId", "stateId"})
public class FrontLineWorker {

    @Field(required = true)
    @Column(length = 20)
    private String flwId;

    @Field(name="stateId", required = true)
    private long stateId;

    @Field(required = true)
    private String contactNo;

    @Field(required = true)
    @Column(length = 250)
    private String name;

    @Field(required = true)
    @Column(length = 50)
    private String type;

    @Field(required = true)
    private long operatorId;

    @Field(name="districtId", required = true)
    private long districtId;

    @Field(name="talukaId")
    @Column(length = 50)
    private String talukaId;

    @Field
    @Column(name="healthBlockId")
    private Long healthBlockId;

    @Field(name="phcId")
     private Long phcId;

    @Field(name="subCentreId")
    private Long subCentreId;

    @Field(name="villageId")
    private Long villageId;

    @Field
    @Column(length = 10)
    private String ashaNumber;

    @Field
    private boolean isValidated;

    @Field
    private Long aadhaarNumber;

    @Field(required = true)
    private DateTime registrationDate;

    @Field(required = true)
    @Column(length = 20)
    private String status;

    @Field(required = true)
    @Column(length = 20)
    private String languageLocationCode;


    public FrontLineWorker(String flwId, long stateId, String contactNo, String name, String type, long operatorId,
                           long districtId, DateTime registrationDate, String status, String languageLocationCode) {
        this.flwId = flwId;
        this.stateId = stateId;
        this.contactNo = contactNo;
        this.name = name;
        this.type = type;
        this.operatorId = operatorId;
        this.districtId = districtId;
        this.registrationDate = registrationDate;
        this.status = status;
        this.languageLocationCode = languageLocationCode;
    }

    public String getFlwId() {
        return flwId;
    }

    public void setFlwId(String flwId) {
        this.flwId = flwId;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
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

    public String getAshaNumber() {
        return ashaNumber;
    }

    public void setAshaNumber(String ashaNumber) {
        this.ashaNumber = ashaNumber;
    }

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean isValidated) {
        this.isValidated = isValidated;
    }

    public long getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(long aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public DateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(DateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(String languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public String getTalukaId() {
        return talukaId;
    }

    public void setTalukaId(String talukaId) {
        this.talukaId = talukaId;
    }
}
