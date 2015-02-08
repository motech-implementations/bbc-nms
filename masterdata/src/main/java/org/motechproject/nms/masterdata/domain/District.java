package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.jdo.annotations.Persistent;
import java.util.Set;

/**
 * This class Models data for District location records
 */
@Entity(recordHistory = true)
public class District extends LocationUnitMetaData {

    @Field
    @Cascade(delete = true)
    @Persistent(defaultFetchGroup = "true")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof District)) {
            return false;
        }

        District district = (District) o;

        if (!districtCode.equals(district.districtCode)) {
            return false;
        }
        if (!stateCode.equals(district.stateCode)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = districtCode != null ? districtCode.hashCode() : 0;
        result = 31 * result + (stateCode != null ? stateCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "District{" +
                "districtCode=" + districtCode +
                ", taluka=" + taluka +
                ", stateCode=" + stateCode +
                '}';
    }
}
