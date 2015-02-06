package org.motechproject.nms.frontlineworker.domain;

import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;
import org.motechproject.mds.event.CrudEventType;

/**
 * Created by abhishek on 26/1/15.
 * This class Models data records provided in the Front Line Worker Csv Upload
 */

@Entity(recordHistory = true)
@CrudEvents(CrudEventType.CREATE)
public class FrontLineWorkerCsv extends MdsEntity {

    @Field
    private String flwId;

    @Field
    private String stateCode;

    @Field
    private String contactNo;

    @Field
    private String name;

    @Field
    private String type;

    @Field
    private String districtCode;

    @Field
    private String talukaCode;

    @Field
    private String healthBlockCode;

    @Field
    private String phcCode;

    @Field
    private String subCentreCode;

    @Field
    private String villageCode;

    @Field
    private String ashaNumber;

    @Field
    private String isValidated;

    @Field
    private String adhaarNo;

    @Field
    private String operation;

    @Field
    private String isValid;

    public String getFlwId() {
        return flwId;
    }

    public void setFlwId(String flwId) {
        this.flwId = flwId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
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

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getTalukaCode() {
        return talukaCode;
    }

    public void setTalukaCode(String talukaCode) {
        this.talukaCode = talukaCode;
    }

    public String getHealthBlockCode() {
        return healthBlockCode;
    }

    public void setHealthBlockCode(String healthBlockCode) {
        this.healthBlockCode = healthBlockCode;
    }

    public String getPhcCode() {
        return phcCode;
    }

    public void setPhcCode(String phcCode) {
        this.phcCode = phcCode;
    }

    public String getSubCentreCode() {
        return subCentreCode;
    }

    public void setSubCentreCode(String subCentreCode) {
        this.subCentreCode = subCentreCode;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
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

    public String getAdhaarNo() {
        return adhaarNo;
    }

    public void setAdhaarNo(String adhaarNo) {
        this.adhaarNo = adhaarNo;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
}
