package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Created by abhishek on 24/1/15.
 */

@Entity(
        recordHistory = true
)
public class HealthFacilityCsv extends LocationUnitMetaDataCsv {

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


    public HealthFacilityCsv(String operation, String name, String healthFacilityCode, String stateCode, String districtCode, String talukaCode, String healthBlockCode) {
        super(operation, name);
        this.healthFacilityCode = healthFacilityCode;
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.talukaCode = talukaCode;
        this.healthBlockCode = healthBlockCode;
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


    @Override
    public String toString() {
        return "HealthFacilityCsv{" +

                ", stateCode=" + stateCode +
                ", districtCode=" + districtCode +
                ", talukaCode=" + talukaCode +
                ", healthBlockCode=" + healthBlockCode +
                ", facilityCode=" + healthFacilityCode +
                '}';
    }
}
