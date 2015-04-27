package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data records provided in the HealthSubFacility Csv Upload
 */

@Entity
public class CsvHealthSubFacility extends MdsEntity {

    @Field
    private String name;

    @Field
    private String healthFacilityCode;

    @Field
    private String healthSubFacilityCode;

    @Field
    private String stateCode;

    @Field
    private String districtCode;

    @Field
    private String talukaCode;

    @Field
    private String healthBlockCode;

    public CsvHealthSubFacility(String name, String healthFacilityCode, String healthSubFacilityCode, String stateCode, String districtCode, String talukaCode, String healthBlockCode) {
        this.name = name;
        this.healthFacilityCode = healthFacilityCode;
        this.healthSubFacilityCode = healthSubFacilityCode;
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.talukaCode = talukaCode;
        this.healthBlockCode = healthBlockCode;
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

    public String getHealthSubFacilityCode() {
        return healthSubFacilityCode;
    }

    public void setHealthSubFacilityCode(String healthSubFacilityCode) {
        this.healthSubFacilityCode = healthSubFacilityCode;
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

    /**
     * This method override the toString method to create string for Health Sub facility Code, State Code
     * District Code, Taluka Code, Health Block Code and
     * Health Facility Code for the instance variables
     *
     * @return The string of the Health Sub facility Code, State Code
     * District Code, Taluka Code, Health Block Code and
     * Health Facility Code of the instance variables.
     */
    @Override
    public String toString() {
        return "HealthSubFacilityCsv{" +
                "name='" + name + '\'' +
                ", healthFacilityCode='" + healthFacilityCode + '\'' +
                ", healthSubFacilityCode='" + healthSubFacilityCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", talukaCode='" + talukaCode + '\'' +
                ", healthBlockCode='" + healthBlockCode + '\'' +
                '}';
    }
}
