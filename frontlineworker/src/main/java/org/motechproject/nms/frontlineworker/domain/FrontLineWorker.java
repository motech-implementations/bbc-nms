package org.motechproject.nms.frontlineworker.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;
import org.motechproject.nms.masterdata.domain.*;

import javax.jdo.annotations.Unique;
/**
 * Created by abhishek on 26/1/15.
 */

@Entity(recordHistory = true)
public class FrontLineWorker extends MdsEntity {


    @Field
    private long flwId;

    @Field(required = true)
    private State state;

    @Field(required = true)
    @Unique
    private String contactNo;

    @Field(required = true)
    private String name;

    @Field(required = true)
    private String designation;

    @Field(required = true)
    private long operatorId;

    @Field(required = true)
    private District district;

    @Field
    private Taluka taluka;

    @Field
    private HealthBlock healthBlock;

    @Field
     private HealthFacility healthFacility;

    @Field
    private HealthSubFacility healthSubFacility;

    @Field
    private Village villageCode;

    @Field
    private String ashaNumber;

    @Field
    private boolean isValidated;

    @Field
    private boolean isValid;

    @Field
    private Long adhaarNumber;

    @Field(required = true)
    private String status;

    @Field(required = true)
    private LanguageLocationCode languageLocationCode;

    public long getFlwId() {
        return flwId;
    }

    public void setFlwId(long flwId) {
        this.flwId = flwId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(long operatorId) {
        this.operatorId = operatorId;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Taluka getTaluka() {
        return taluka;
    }

    public void setTaluka(Taluka taluka) {
        this.taluka = taluka;
    }

    public HealthBlock getHealthBlock() {
        return healthBlock;
    }

    public void setHealthBlock(HealthBlock healthBlock) {
        this.healthBlock = healthBlock;
    }

    public HealthFacility getHealthFacility() {
        return healthFacility;
    }

    public void setHealthFacility(HealthFacility healthFacility) {
        this.healthFacility = healthFacility;
    }

    public HealthSubFacility getHealthSubFacility() {
        return healthSubFacility;
    }

    public void setHealthSubFacility(HealthSubFacility healthSubFacility) {
        this.healthSubFacility = healthSubFacility;
    }

    public Village getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(Village villageCode) {
        this.villageCode = villageCode;
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

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public Long getAdhaarNumber() {
        return adhaarNumber;
    }

    public void setAdhaarNumber(Long adhaarNumber) {
        this.adhaarNumber = adhaarNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LanguageLocationCode getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(LanguageLocationCode languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }
}
