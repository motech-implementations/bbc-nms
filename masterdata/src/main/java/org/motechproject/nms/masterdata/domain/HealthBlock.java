package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Set;

/**
 * Created by abhishek on 24/1/15.
 */
@Entity(recordHistory = true)
public class HealthBlock extends LocationUnitMetaData {

    @Field
    private Long healthBlockId;

    @Field
    private Set<HealthFacility> healthBlock;

    @Field
    private String talukaCode;

    @Field
    private Long districtCode;

    @Field
    private Long stateCode;

    public HealthBlock(String name, Long healthBlockId, Set<HealthFacility> healthBlock, String talukaCode, Long districtCode, Long stateCode) {
        super(name);
        this.healthBlockId = healthBlockId;
        this.healthBlock = healthBlock;
        this.talukaCode = talukaCode;
        this.districtCode = districtCode;
        this.stateCode = stateCode;
    }

    public Long getHealthBlockId() {
        return healthBlockId;
    }

    public void setHealthBlockId(Long healthBlockId) {
        this.healthBlockId = healthBlockId;
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