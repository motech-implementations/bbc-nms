package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Created by abhishek on 24/1/15.
 */

@Entity
public class HealthFacilityCsv extends LocationUnitMetaDataCsv{

    @Field
    private String phcId;

    @Field
    private String stateId;

    @Field
    private String districtId;

    @Field
    private String tCode;

    @Field
    private String healthBlockId;

    @Field
    private String facilityType;

    public HealthFacilityCsv(String operation, String name, String phcId,String stateId,String districtId,String tCode,String healthBlockId,String facilityType) {
        super(operation, name);
        this.phcId = phcId;
        this.stateId = stateId;
        this.districtId = districtId;
        this.tCode = tCode;
        this.healthBlockId = healthBlockId;
        this.facilityType = facilityType;
    }

    public String getPhcId() {
        return phcId;
    }

    public void setPhcId(String phcId) {
        this.phcId = phcId;
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

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

}
