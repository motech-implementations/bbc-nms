package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import java.util.Set;

/**
 * This class Models data for HealthBlock location records
 */
@Entity(recordHistory = true)
public class HealthBlock extends LocationUnitMetaData {

    @Field
    @Cascade(delete = true)
    private Set<HealthFacility> healthFacility;

    @Field
    @UIDisplayable(position = 4)
    private Long stateCode;

    @Field
    @UIDisplayable(position = 3)
    private Long districtCode;

    @Field
    @UIDisplayable(position = 2)
    private Long talukaCode;

    @Field
    @UIDisplayable(position = 1)
    private Long healthBlockCode;


    public HealthBlock() {

    }

    public Long getHealthBlockCode() {
        return healthBlockCode;
    }

    public void setHealthBlockCode(Long healthBlockCode) {
        this.healthBlockCode = healthBlockCode;
    }

    public Set<HealthFacility> getHealthFacility() {
        return healthFacility;
    }

    public void setHealthFacility(Set<HealthFacility> healthFacility) {
        this.healthFacility = healthFacility;
    }

    public Long getTalukaCode() {
        return talukaCode;
    }

    public void setTalukaCode(Long talukaCode) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HealthBlock)) {
            return false;
        }

        HealthBlock that = (HealthBlock) o;

        if (!this.getDistrictCode().equals(that.getDistrictCode())) {
            return false;
        }
        if (!this.getHealthBlockCode().equals(that.getHealthBlockCode())) {
            return false;
        }
        if (!this.getStateCode().equals(that.getStateCode())) {
            return false;
        }
        if (!this.getTalukaCode().equals(that.getTalukaCode())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = stateCode.hashCode();
        result = 31 * result + districtCode.hashCode();
        result = 31 * result + talukaCode.hashCode();
        result = 31 * result + healthBlockCode.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "HealthBlock{" +
                "healthFacility=" + healthFacility +
                ", stateCode=" + stateCode +
                ", districtCode=" + districtCode +
                ", talukaCode='" + talukaCode + '\'' +
                ", healthBlockCode=" + healthBlockCode +
                '}';
    }
}
