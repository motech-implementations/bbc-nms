package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;

import java.util.Set;

/**
 * This class Models data for Taluka location records
 */
@Entity(recordHistory = true)
public class Taluka extends MdsEntity {

    @Field
    @UIDisplayable(position = 0)
    private String name;

    @Field
    @UIDisplayable(position = 3)
    private Long stateCode;

    @Field
    @UIDisplayable(position = 2)
    private Long districtCode;

    @Field
    @UIDisplayable(position = 1)
    private Long talukaCode;

    @Field
    @Cascade(delete = true)
    private Set<HealthBlock> healthBlock;

    @Field
    @Cascade(delete = true)
    private Set<Village> village;


    public Taluka() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    /**
     * Calculates the hash code according to the District Code, State Code and
     * Taluka code
     *
     * @return An int hash value
     */
    @Override
    public int hashCode() {
        int result = stateCode.hashCode();
        result = 31 * result + districtCode.hashCode();
        result = 31 * result + talukaCode.hashCode();
        return result;
    }

    /**
     * This method override the toString method to create string for name, state code
     * District code, taluka code, health block and village for the instance variables
     *
     * @return The string of the name, state code
     * District code, taluka code, health block and village for the instance variables
     */
    @Override
    public String toString() {
        return "Taluka{" +
                "name='" + name + '\'' +
                ", stateCode=" + stateCode +
                ", districtCode=" + districtCode +
                ", talukaCode=" + talukaCode +
                ", healthBlock=" + healthBlock +
                ", village=" + village +
                '}';
    }
}
