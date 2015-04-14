package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data records provided in the HealthBlock Csv Upload
 */
@Entity
public class CsvHealthBlock extends MdsEntity {

    @Field
    private String name;

    @Field
    private String healthBlockCode;

    @Field
    private String stateCode;

    @Field
    private String districtCode;

    @Field
    private String talukaCode;

    public CsvHealthBlock(String name, String healthBlockCode, String stateCode, String districtCode, String talukaCode) {
        this.name = name;
        this.healthBlockCode = healthBlockCode;
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.talukaCode = talukaCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHealthBlockCode() {
        return healthBlockCode;
    }

    public void setHealthBlockCode(String healthBlockCode) {
        this.healthBlockCode = healthBlockCode;
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

    /**
     * This method override the toString method to create string for State Code
     * District Code, Taluka Code and Health Block Code for the instance variables
     *
     * @return The string of the State Code District Code, Taluka Code and Health Block Code of the instance variables.
     */
    @Override
    public String toString() {
        return "HealthBlockCsv{" +
                "name='" + name + '\'' +
                ", healthBlockCode='" + healthBlockCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", talukaCode='" + talukaCode + '\'' +
                '}';
    }
}
