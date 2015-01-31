package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Set;

/**
 * Created by abhishek on 24/1/15.
 */
@Entity(recordHistory = true)
public class HealthFacility extends LocationUnitMetaData{


    @Field
    private Long phcId;

    @Field
    private Integer facilityType;

    @Field
    private Set<HealthSubFacility> healthSubFacility;

    @Field
    private Long healthBlockId;

    @Field
    private String talukaCode;

    @Field
    private Long districtCode;

    @Field
    private Long stateCode;

    public HealthFacility(String name, Long phcId, Integer facilityType, Set<HealthSubFacility> healthSubFacility, Long healthBlockId, String talukaCode, Long districtCode, Long stateCode) {
        super(name);
        this.phcId = phcId;
        this.facilityType = facilityType;
        this.healthSubFacility = healthSubFacility;
        this.healthBlockId = healthBlockId;
        this.talukaCode = talukaCode;
        this.districtCode = districtCode;
        this.stateCode = stateCode;
    }

    public Long getPhcId() {
        return phcId;
    }

    public void setPhcId(Long phcId) {
        this.phcId = phcId;
    }

    public Integer getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(Integer facilityType) {
        this.facilityType = facilityType;
    }

    public Set<HealthSubFacility> getHealthSubFacility() {
        return healthSubFacility;
    }

    public void setHealthSubFacility(Set<HealthSubFacility> healthSubFacility) {
        this.healthSubFacility = healthSubFacility;
    }

    public Long getHealthBlockId() {
        return healthBlockId;
    }

    public void setHealthBlockId(Long healthBlockId) {
        this.healthBlockId = healthBlockId;
    }

    public String getTalukaCode() {
        return talukaCode;
    }

    public void setTalukaCode(String talukaCode) {
        this.talukaCode = talukaCode;
    }

    public Long getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(Long districtCode) {
        this.districtCode = districtCode;
    }

    public Long getStateCode() {
        return stateCode;
    }

    public void setStateCode(Long stateCode) {
        this.stateCode = stateCode;
    }
}
