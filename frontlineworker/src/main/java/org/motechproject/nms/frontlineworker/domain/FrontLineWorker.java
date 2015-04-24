package org.motechproject.nms.frontlineworker.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;
import org.motechproject.nms.frontlineworker.enums.Designation;
import org.motechproject.nms.frontlineworker.enums.Status;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;

/**
 * This class Models data for Front Line Worker records
 */

@Entity(recordHistory = true)
public class FrontLineWorker extends MdsEntity {

    @UIDisplayable(position = 0)
    @Field
    private Long flwId;

    @UIDisplayable(position = 2)
    @Field(required = true)
    private String contactNo;

    @UIDisplayable(position = 1)
    @Field
    private String name;

    @UIDisplayable(position = 3)
    @Field
    private Designation designation;

    @Field
    private String operatorCode;

    @UIDisplayable(position = 4)
    @Field
    private Long stateCode;

    @Field(name = "state_id")
    private State stateId;

    @Field(name = "district_id")
    private District districtId;

    @Field(name = "taluka_id")
    private Taluka talukaId;

    @Field(name = "healthBlock_id")
    private HealthBlock healthBlockId;

    @Field(name = "phc_id")
    private HealthFacility healthFacilityId;

    @Field(name = "subCentre_id")
    private HealthSubFacility healthSubFacilityId;

    @Field(name = "village_id")
    private Village villageId;

    @UIDisplayable(position = 9)
    @Field
    private String adhaarNumber;

    @UIDisplayable(position = 5)
    @Field(required = true)
    private Status status;

    @UIDisplayable(position = 6)
    @Field
    private String languageLocationCodeId = null;

    @UIDisplayable(position = 7)
    @Field
    private String circleCode = null;

    @Field
    private String alternateContactNo;

    @Field
    private String oldMobileNo;

    @Field
    private DateTime invalidDate;

    public FrontLineWorker() {
    }

    public FrontLineWorker(Long flwId, String contactNo, String name, Designation designation, String operatorCode,
                           Long stateCode, State stateId, District districtId, Taluka talukaId,
                           HealthBlock healthBlockId, HealthFacility healthFacilityId, HealthSubFacility
            healthSubFacilityId, Village villageId, String adhaarNumber,
                           Status status, String languageLocationCodeId, String circleCode, String alternateContactNo,
                           String oldMobileNo, DateTime invalidDate) {
        this.flwId = flwId;
        this.contactNo = contactNo;
        this.name = name;
        this.designation = designation;
        this.operatorCode = operatorCode;
        this.stateCode = stateCode;
        this.stateId = stateId;
        this.districtId = districtId;
        this.talukaId = talukaId;
        this.healthBlockId = healthBlockId;
        this.healthFacilityId = healthFacilityId;
        this.healthSubFacilityId = healthSubFacilityId;
        this.villageId = villageId;
        this.adhaarNumber = adhaarNumber;
        this.status = status;
        this.languageLocationCodeId = languageLocationCodeId;
        this.circleCode = circleCode;
        this.alternateContactNo = alternateContactNo;
        this.oldMobileNo = oldMobileNo;
        this.invalidDate = invalidDate;
    }

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

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
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

    public String getLanguageLocationCodeId() {
        return languageLocationCodeId;
    }

    public void setLanguageLocationCodeId(String languageLocationCodeId) {
        this.languageLocationCodeId = languageLocationCodeId;
    }

    public String getCircleCode() {
        return circleCode;
    }

    public void setCircleCode(String circleCode) {
        this.circleCode = circleCode;
    }

    public String getAlternateContactNo() {
        return alternateContactNo;
    }

    public void setAlternateContactNo(String alternateContactNo) {
        this.alternateContactNo = alternateContactNo;
    }

    public String getOldMobileNo() {
        return oldMobileNo;
    }

    public void setOldMobileNo(String oldMobileNo) {
        this.oldMobileNo = oldMobileNo;
    }

    public DateTime getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(DateTime invalidDate) {
        this.invalidDate = invalidDate;
    }
}
