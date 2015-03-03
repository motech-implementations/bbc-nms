package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class Models data for HealthBlock location records
 */
@Entity(recordHistory = true)
public class HealthBlock extends MdsEntity {

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

    @Field
    @UIDisplayable(position = 0)
    private String name;

    public HealthBlock() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    /**
     * Calculates the hash code according to the District Code Taluka Code and Health Block Code
     *
     * @return An int hash value
     */
    @Override
    public int hashCode() {
        List<Long> list=new ArrayList<Long>();
        int result = stateCode.hashCode();
        list.add(districtCode);
        list.add(talukaCode);
        list.add(healthBlockCode);
        result=HashCode.hashCode(list,result);
        return result;
    }

    /**
     * This method override the toString method to create string for Health facility, State Code
     * District Code, Taluka Code and Health Block Code for the instance variables
     *
     * @return The string of the Health facility, State Code District Code, Taluka Code and Health Block Code of the instance variables.
     */
    @Override
    public String toString() {
        return "HealthBlock{" +
                "healthFacility=" + healthFacility +
                ", stateCode=" + stateCode +
                ", districtCode=" + districtCode +
                ", talukaCode=" + talukaCode +
                ", healthBlockCode=" + healthBlockCode +
                ", name='" + name + '\'' +
                '}';
    }
}
