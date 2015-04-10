package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;

import java.util.Set;

/**
 * This class Models data for District location records
 */
@Entity(recordHistory = true)
public class District extends MdsEntity {

    @Field
    @UIDisplayable(position = 0)
    private String name;

    @Field
    @Cascade(delete = true)
    private Set<Taluka> taluka;

    @Field
    @UIDisplayable(position = 2)
    private Long stateCode;

    @Field
    @UIDisplayable(position = 1)
    private Long districtCode;


    public District() {
        super();
    }

    public Long getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(Long districtCode) {
        this.districtCode = districtCode;
    }

    public Set<Taluka> getTaluka() {
        return taluka;
    }

    public void setTaluka(Set<Taluka> taluka) {
        this.taluka = taluka;
    }

    public Long getStateCode() {
        return stateCode;
    }

    public void setStateCode(Long stateCode) {
        this.stateCode = stateCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof District)) {
            return false;
        }

        District district = (District) o;

        if (!this.getDistrictCode().equals(district.getDistrictCode())) {
            return false;
        }
        if (!this.getStateCode().equals(district.getStateCode())) {
            return false;
        }

        return true;
    }

    /**
     * Calculates the hash code according to the District Code and State Code
     *
     * @return An int hash value
     */
    @Override
    public int hashCode() {
        int result = districtCode != null ? districtCode.hashCode() : 0;
        result = 31 * result + (stateCode != null ? stateCode.hashCode() : 0);
        return result;
    }

    /**
     * This method override the toString method to create string for District code, Taluka and
     * State Code for the instance variables
     *
     * @return The string of the District code, Taluka and State Code of the instance variables.
     */
    @Override
    public String toString() {
        return "District{" +
                "name='" + name + '\'' +
                ", taluka=" + taluka +
                ", stateCode=" + stateCode +
                ", districtCode=" + districtCode +
                '}';
    }
}
