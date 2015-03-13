package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import java.util.Set;

/**
 * This class Models data for Taluka location records
 */
@Entity(recordHistory = true)
public class Taluka extends LocationUnitMetaData {

    @Field
    @UIDisplayable(position = 3)
    private Long stateCode;

    @Field
    @UIDisplayable(position = 2)
    private Long districtCode;

    @Field
    @UIDisplayable(position = 1)
    private String talukaCode;

    @Field
    @Cascade(delete = true)
    private Set<HealthBlock> healthBlock;

    @Field
    @Cascade(delete = true)
    private Set<Village> village;


    public Taluka() {
    }

    public Set<HealthBlock> getHealthBlock() {
        return healthBlock;
    }

    public void setHealthBlock(Set<HealthBlock> healthBlock) {
        this.healthBlock = healthBlock;
    }

    public Set<Village> getVillage() {
        return village;
    }

    public void setVillage(Set<Village> village) {
        this.village = village;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Taluka)) {
            return false;
        }

        Taluka taluka = (Taluka) o;

        if (!this.getDistrictCode().equals(taluka.getDistrictCode())) {
            return false;
        }
        if (!this.getStateCode().equals(taluka.getStateCode())) {
            return false;
        }
        if (!this.getTalukaCode().equals(taluka.getTalukaCode())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = stateCode.hashCode();
        result = 31 * result + districtCode.hashCode();
        result = 31 * result + talukaCode.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Taluka{" +
                "stateCode=" + stateCode +
                ", districtCode=" + districtCode +
                ", talukaCode='" + talukaCode + '\'' +
                ", healthBlock=" + healthBlock +
                ", village=" + village +
                '}';
    }
}
