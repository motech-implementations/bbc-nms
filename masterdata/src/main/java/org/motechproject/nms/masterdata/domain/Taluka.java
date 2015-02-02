package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Set;

/**
 * This class Models data for Taluka location records
 */
@Entity(recordHistory = true)
public class Taluka extends LocationUnitMetaData {

    @Field
    private String talukaCode;

    @Field
    @Cascade(delete = true)
    private Set<HealthBlock> healthBlock;

    @Field
    @Cascade(delete = true)
    private Set<Village> village;

    @Field
    private Long districtCode;

    @Field
    private Long stateCode;

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
    public String toString() {
        return "Taluka{" +
                "talukaCode='" + talukaCode + '\'' +
                ", healthBlock=" + healthBlock +
                ", village=" + village +
                ", districtCode=" + districtCode +
                ", stateCode=" + stateCode +
                '}';
    }
}
