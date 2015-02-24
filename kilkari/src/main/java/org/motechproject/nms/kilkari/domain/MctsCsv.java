package org.motechproject.nms.kilkari.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This entity represents the mother mcts record.
 */
@Entity
public class MctsCsv extends MdsEntity {
    
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
    private String whomPhoneNo;

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

    public String getWhomPhoneNo() {
        return whomPhoneNo;
    }

    public void setWhomPhoneNo(String whomPhoneNo) {
        this.whomPhoneNo = whomPhoneNo;
    }
    
}
