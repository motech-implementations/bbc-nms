package org.motechproject.nms.frontlineworker.event.handler;

/**
 * Created by abhishek on 6/2/15.
 */
public class TemporaryFrontLineWorker {

    private Long flwId;

    private Long stateCode;

    private String contactNo;

    private String name;

    private String designation;

    private Long operatorId;

    private Long districtCode;

    private String talukaCode;

    private Long healthBlock;

    private Long healthFacility;

    private Long healthSubFacility;

    private Long villageCode;

    private String ashaNumber;

    private boolean isValidated;

    private boolean isValid;

    private String adhaarNumber;

    private String status;

    private Long languageLocationCodeId;

    public void setFlwId(Long flwId) {
        this.flwId = flwId;
    }

    public Long getFlwId() {
        return flwId;
    }

    public Long getStateCode() {
        return stateCode;
    }

    public void setStateCode(Long stateCode) {
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

    public Long getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(Long districtCode) {
        this.districtCode = districtCode;
    }

    public String getTalukaCode() {
        return talukaCode;
    }

    public void setTalukaCode(String talukaCode) {
        this.talukaCode = talukaCode;
    }

    public Long getHealthBlock() {
        return healthBlock;
    }

    public void setHealthBlock(Long healthBlock) {
        this.healthBlock = healthBlock;
    }

    public Long getHealthFacility() {
        return healthFacility;
    }

    public void setHealthFacility(Long healthFacility) {
        this.healthFacility = healthFacility;
    }

    public Long getHealthSubFacility() {
        return healthSubFacility;
    }

    public void setHealthSubFacility(Long healthSubFacility) {
        this.healthSubFacility = healthSubFacility;
    }

    public Long getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(Long villageCode) {
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

    public String getAdhaarNumber() {
        return adhaarNumber;
    }

    public void setAdhaarNumber(String adhaarNumber) {
        this.adhaarNumber = adhaarNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getLanguageLocationCodeId() {
        return languageLocationCodeId;
    }

    public void setLanguageLocationCodeId(Long languageLocationCodeId) {
        this.languageLocationCodeId = languageLocationCodeId;
    }
}
