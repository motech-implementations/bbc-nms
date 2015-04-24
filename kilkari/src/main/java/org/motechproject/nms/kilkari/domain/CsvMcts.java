package org.motechproject.nms.kilkari.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This entity represents the mcts csv record fields common in
 * Mother and Child data.
 */
@Entity
public class CsvMcts extends MdsEntity {
    
    @Field
    private String stateCode;

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
    private String idNo;
    
    @Field
    private String whomPhoneNo;
    
    @Field
    private String entryType;

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
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

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }
    
}
