package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data records provided in the HealthFacility Csv Upload
 */

@Entity
public class HealthFacilityCsv extends MdsEntity {

    @Field
    private String name;

    @Field
    private String healthFacilityCode;

    @Field
    private String stateCode;

    @Field
    private String districtCode;

    @Field
    private String talukaCode;

    @Field
    private String healthBlockCode;

    @Field
    private String healthFacilityType;


    public HealthFacilityCsv(String name, String healthFacilityCode, String stateCode, String districtCode, String talukaCode, String healthBlockCode, String healthFacilityType) {
        this.name = name;
        this.healthFacilityCode = healthFacilityCode;
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.talukaCode = talukaCode;
        this.healthBlockCode = healthBlockCode;
        this.healthFacilityType = healthFacilityType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHealthFacilityCode() {
        return healthFacilityCode;
    }

    public void setHealthFacilityCode(String healthFacilityCode) {
        this.healthFacilityCode = healthFacilityCode;
    }

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

    public String getHealthFacilityType() {
        return healthFacilityType;
    }

    public void setHealthFacilityType(String healthFacilityType) {
        this.healthFacilityType = healthFacilityType;
    }

    /**
     * This method override the toString method to create string for State Code
     * District Code, Taluka Code, Health Block Code, Health Facility Type and
     * Health Facility Code for the instance variables
     *
     * @return The string of the State Code
     * District Code, Taluka Code, Health Block Code, Health Facility Type and
     * Health Facility Code of the instance variables.
     */
    @Override
    public String toString() {
        return "HealthFacilityCsv{" +
                "name='" + name + '\'' +
                ", healthFacilityCode='" + healthFacilityCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", talukaCode='" + talukaCode + '\'' +
                ", healthBlockCode='" + healthBlockCode + '\'' +
                ", healthFacilityType='" + healthFacilityType + '\'' +
                '}';
    }
}
