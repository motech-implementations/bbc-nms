package org.motechproject.nms.frontlineworker.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;
import org.motechproject.nms.frontlineworker.Designation;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jdo.annotations.Unique;

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
    @Unique
    private String contactNo;

    @UIDisplayable(position = 1)
    @Field(required = true)
    private String name;

    @UIDisplayable(position = 3)
    @Field(required = true)
    private Designation designation;

    @Field
    private Long operatorId;

    @UIDisplayable(position = 4)
    @Field(required = true)
    private Long stateCode;

    @Field(required = true, name = "state_id")
    private State stateId;

    @Field(required = true, name = "district_id")
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

    @UIDisplayable(position = 7)
    @Field
    private String ashaNumber;

    @UIDisplayable(position = 8)
    @Field
    private String adhaarNumber;

    @UIDisplayable(position = 5)
    @Field(required = true)
    private Status status;

    @UIDisplayable(position = 6)
    @Field
    private Long languageLocationCodeId = null;

    private static Logger logger = LoggerFactory.getLogger(FrontLineWorker.class);

    public FrontLineWorker() {
    }

    public FrontLineWorker(Long flwId, String contactNo, String name, Designation designation, Long operatorId,
                           Long stateCode, State stateId, District districtId, Taluka talukaId, HealthBlock
            healthBlockId, HealthFacility healthFacilityId, HealthSubFacility healthSubFacilityId,
                           Village villageId, String ashaNumber, String adhaarNumber,
                           Status status, Long languageLocationCodeId) {
        this.flwId = flwId;
        this.contactNo = contactNo;
        this.name = name;
        this.designation = designation;
        this.operatorId = operatorId;
        this.stateCode = stateCode;
        this.stateId = stateId;
        this.districtId = districtId;
        this.talukaId = talukaId;
        this.healthBlockId = healthBlockId;
        this.healthFacilityId = healthFacilityId;
        this.healthSubFacilityId = healthSubFacilityId;
        this.villageId = villageId;
        this.ashaNumber = ashaNumber;
        this.adhaarNumber = adhaarNumber;
        this.status = status;
        this.languageLocationCodeId = languageLocationCodeId;
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


    /**
     * This method maps a field of FrontLineWorkerCsv type to FrontLineWorker field. It checks for null/empty values,
     * and raises exception if a mandatory field is empty/null or is invalid date format
     */
    public void mapFrontLineWorkerFromCsvRecord(FrontLineWorkerCsv record) throws DataValidationException {


        logger.debug("mapFrontLineWorkerFrom process start");

        this.setName(ParseDataHelper.validateAndParseString("Name", record.getName(), true));

        this.setFlwId(ParseDataHelper.validateAndParseLong("Flw Id", record.getFlwId(), false));
        this.setAshaNumber(ParseDataHelper.validateAndParseString("Asha Number", record.getAshaNumber(), false));
        this.setAdhaarNumber(ParseDataHelper.validateAndParseString("Adhaar Number", record.getAdhaarNo(), false));

        if (record.getIsValid().equalsIgnoreCase("false")) {
            this.setStatus(Status.INVALID);
        } else {
            this.setStatus(Status.INACTIVE);
        }

        this.setCreator(record.getCreator());
        this.setModifiedBy(record.getModifiedBy());
        this.setOwner(record.getOwner());
        this.setModificationDate(record.getModificationDate());
        this.setCreationDate(record.getCreationDate());

        logger.debug("mapFrontLineWorkerFrom process end");
    }


    /**
     * This method maps fields of generated front line worker object to front line worker object that
     * is to be saved in Database.
     *
     * @param dbRecord        the record which will be updated in db
     */
    public FrontLineWorker updateDbRecord(FrontLineWorker dbRecord) {

        dbRecord.setName(this.getName());
        dbRecord.setStatus(this.getStatus());
        dbRecord.setContactNo(this.getContactNo());
        dbRecord.setDesignation(this.getDesignation());


        dbRecord.setStateCode(this.getStateCode());
        dbRecord.setStateId(this.getStateId());
        dbRecord.setDistrictId(this.getDistrictId());
        dbRecord.setTalukaId(this.getTalukaId());
        dbRecord.setVillageId(this.getVillageId());
        dbRecord.setHealthBlockId(this.getHealthBlockId());
        dbRecord.setHealthFacilityId(this.getHealthFacilityId());
        dbRecord.setHealthSubFacilityId(this.getHealthSubFacilityId());
        dbRecord.setLanguageLocationCodeId(this.getLanguageLocationCodeId());

        dbRecord.setFlwId(this.getFlwId());
        dbRecord.setAdhaarNumber(this.getAdhaarNumber());
        dbRecord.setAshaNumber(this.getAshaNumber());

        dbRecord.setCreator(this.getCreator());
        dbRecord.setModificationDate(this.getModificationDate());
        dbRecord.setModifiedBy(this.getModifiedBy());
        dbRecord.setCreationDate(this.getCreationDate());
        dbRecord.setOwner(this.getOwner());
        return dbRecord;

    }


}
