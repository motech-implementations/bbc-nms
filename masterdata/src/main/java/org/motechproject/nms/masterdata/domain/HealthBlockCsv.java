package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * This class Models data records provided in the HealthBlock Csv Upload
 */
@Entity(
        recordHistory = true
)

public class HealthBlockCsv extends LocationUnitMetaDataCsv {

    @Field
    private String healthBlockCode;

    @Field
    private String stateCode;

    @Field
    private String districtCode;

    @Field
    private String talukaCode;


    public HealthBlockCsv(String operation, String name, String healthBlockCode, String stateCode, String districtCode, String talukaCode) {
        super(operation, name);
        this.healthBlockCode = healthBlockCode;
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.talukaCode = talukaCode;
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

    @Override
    public String toString() {
        return "HealthBlockCsv{" +
                "healthBlockCode=" + healthBlockCode +
                ", stateCode=" + stateCode +
                ", districtCode=" + districtCode +
                ", talukaCode=" + talukaCode +
                '}';
    }
}
