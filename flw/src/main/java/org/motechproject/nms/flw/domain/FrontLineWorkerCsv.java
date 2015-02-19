package org.motechproject.nms.flw.domain;

import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.event.CrudEventType;

import javax.jdo.annotations.Column;

import static org.motechproject.nms.flw.FrontLineWorkerConstants.*;
/**
 * Created by abhishek on 26/1/15.
 */

@Entity
@CrudEvents(CrudEventType.CREATE)
public class FrontLineWorkerCsv {

    @Field
    private String flwId;

    @Field
    private String stateId;

    @Field
    private String contactNumber;

    @Field
    private String name;

    @Field
    private String type;

    @Field
    private String districtId;

    @Field
    private String tCode;

    @Field
    private String healthBlockId;

    @Field
    private String phcId;

    @Field
    private String subCentreId;

    @Field
    private String villageId;

    @Field
    @Column(length = FLW_ASHA_NUMBER_LENGTH)
    private String ashaNumber;

    @Field
    private String isValidated;

    @Field
    private String aadhaarNumber;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String gettCode() {
        return tCode;
    }

    public void settCode(String tCode) {
        this.tCode = tCode;
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

    public String getAshaNumber() {
        return ashaNumber;
    }

    public void setAshaNumber(String ashaNumber) {
        this.ashaNumber = ashaNumber;
    }

    public String getIsValidated() {
        return isValidated;
    }

    public void setIsValidated(String isValidated) {
        this.isValidated = isValidated;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }
}
