package org.motechproject.nms.frontlineworker.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;

import javax.jdo.annotations.Unique;

/**
 * This class Models data for Front Line Worker records
 */

@Entity(recordHistory = true)
public class FrontLineWorker extends MdsEntity {


    @Field
    private Long flwId;

    @Field(required = true)
    @Unique
    private String contactNo;

    @Field(required = true)
    private String name;

    @Field(required = true)
    private String designation;

    @Field
    private Long operatorId;

    @Field(required = true)
    private Long stateCode;

    @Field(required = true, name = "state_Id")
    private State stateId;

    @Field(required = true, name = "district_Id")
    private District districtId;

    @Field(name = "taluka_Id")
    private Taluka talukaId;

    @Field(name = "healthBlock_Id")
    private HealthBlock healthBlockId;

    @Field(name = "healthFacility_Id")
    private HealthFacility healthFacilityId;

    @Field(name = "healthSubFacility_Id")
    private HealthSubFacility healthSubFacilityId;

    @Field(name = "village_Id")
    private Village villageId;

    @Field
    private String ashaNumber;

    @Field
    private boolean isValidated;

    @Field
    private String adhaarNumber;

    @Field(required = true)
    private Status status;

    @Field
    private Long languageLocationCodeId;

    public Long getFlwId() {
        return flwId;
    }

    public void setFlwId(Long flwId) {
        this.flwId = flwId;
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

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Long getStateCode() {
        return stateCode;
    }

    public void setStateCode(Long stateCode) {
        this.stateCode = stateCode;
    }

    public State getStateId() {
        return stateId;
    }

    public void setStateId(State stateId) {
        this.stateId = stateId;
    }

    public District getDistrictId() {
        return districtId;
    }

    public void setDistrictId(District districtId) {
        this.districtId = districtId;
    }

    public Taluka getTalukaId() {
        return talukaId;
    }

    public void setTalukaId(Taluka talukaId) {
        this.talukaId = talukaId;
    }

    public HealthBlock getHealthBlockId() {
        return healthBlockId;
    }

    public void setHealthBlockId(HealthBlock healthBlockId) {
        this.healthBlockId = healthBlockId;
    }

    public HealthFacility getHealthFacilityId() {
        return healthFacilityId;
    }

    public void setHealthFacilityId(HealthFacility healthFacilityId) {
        this.healthFacilityId = healthFacilityId;
    }

    public HealthSubFacility getHealthSubFacilityId() {
        return healthSubFacilityId;
    }

    public void setHealthSubFacilityId(HealthSubFacility healthSubFacilityId) {
        this.healthSubFacilityId = healthSubFacilityId;
    }

    public Village getVillageId() {
        return villageId;
    }

    public void setVillageId(Village villageId) {
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

    public String getAdhaarNumber() {
        return adhaarNumber;
    }

    public void setAdhaarNumber(String adhaarNumber) {
        this.adhaarNumber = adhaarNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getLanguageLocationCodeId() {
        return languageLocationCodeId;
    }

    public void setLanguageLocationCodeId(Long languageLocationCodeId) {
        this.languageLocationCodeId = languageLocationCodeId;
    }
}
