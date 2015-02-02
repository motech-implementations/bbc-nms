package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Set;

/**
 * This class Models data for HealthBlock location records
 */
@Entity(recordHistory = true)
public class HealthBlock extends LocationUnitMetaData {

    @Field
    private Long healthBlockCode;

    @Field
    @Cascade(delete = true)
    private Set<HealthFacility> healthBlock;

    @Field
    private String talukaCode;

    @Field
    private Long districtCode;

    @Field
    private Long stateCode;

    public HealthBlock() {

    }

    public Long getHealthBlockCode() {
        return healthBlockCode;
    }

    public void setHealthBlockCode(Long healthBlockCode) {
        this.healthBlockCode = healthBlockCode;
    }

    public Set<HealthFacility> getHealthBlock() {
        return healthBlock;
    }

    public void setHealthBlock(Set<HealthFacility> healthBlock) {
        this.healthBlock = healthBlock;
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