package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

/**
 * This class Models data for HealthSubFacility location records
 */

@Entity(recordHistory = true)
public class HealthSubFacility extends LocationUnitMetaData {

    @Field
    @UIDisplayable(position = 6)
    private Long stateCode;

    @Field
    @UIDisplayable(position = 5)
    private Long districtCode;

    @Field
    @UIDisplayable(position = 4)
    private String talukaCode;

    @Field
    @UIDisplayable(position = 3)
    private Long healthBlockCode;

    @Field
    @UIDisplayable(position = 2)
    private Long healthFacilityCode;

    @Field
    @UIDisplayable(position = 1)
    private Long healthSubFacilityCode;

    public Long getHealthFacilityCode() {
        return healthFacilityCode;
    }

    public void setHealthFacilityCode(Long healthFacilityCode) {
        this.healthFacilityCode = healthFacilityCode;
    }

    public Long getHealthSubFacilityCode() {
        return healthSubFacilityCode;
    }

    public void setHealthSubFacilityCode(Long healthSubFacilityCode) {
        this.healthSubFacilityCode = healthSubFacilityCode;
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

    public Long getHealthBlockCode() {
        return healthBlockCode;
    }

    public void setHealthBlockCode(Long healthBlockCode) {
        this.healthBlockCode = healthBlockCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HealthSubFacility)) return false;

        HealthSubFacility that = (HealthSubFacility) o;

        if (!this.getDistrictCode().equals(that.getDistrictCode())) return false;
        if (!this.getHealthBlockCode().equals(that.getHealthBlockCode())) return false;
        if (!this.getHealthFacilityCode().equals(that.getHealthFacilityCode())) return false;
        if (!this.getHealthSubFacilityCode().equals(that.getHealthSubFacilityCode())) return false;
        if (!this.getStateCode().equals(that.getStateCode())) return false;
        if (!this.getTalukaCode().equals(that.getTalukaCode())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stateCode.hashCode();
        result = 31 * result + districtCode.hashCode();
        result = 31 * result + talukaCode.hashCode();
        result = 31 * result + healthBlockCode.hashCode();
        result = 31 * result + healthFacilityCode.hashCode();
        result = 31 * result + healthSubFacilityCode.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "HealthSubFacility{" +
                "stateCode=" + stateCode +
                ", districtCode=" + districtCode +
                ", talukaCode='" + talukaCode + '\'' +
                ", healthBlockCode=" + healthBlockCode +
                ", healthFacilityCode=" + healthFacilityCode +
                ", healthSubFacilityCode=" + healthSubFacilityCode +
                '}';
    }
}
