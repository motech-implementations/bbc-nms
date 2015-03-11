package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

/**
 * This class Models data for Village location records
 */
@Entity(recordHistory = true)
public class Village extends LocationUnitMetaData {

    @Field
    @UIDisplayable(position = 4)
    private Long stateCode;

    @Field
    @UIDisplayable(position = 3)
    private Long districtCode;

    @Field
    @UIDisplayable(position = 2)
    private Integer talukaCode;

    @Field
    @UIDisplayable(position = 1)
    private Long villageCode;

    public Village() {

    }

    public Long getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(Long villageCode) {
        this.villageCode = villageCode;
    }

    public Integer getTalukaCode() {
        return talukaCode;
    }

    public void setTalukaCode(Integer talukaCode) {
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
        if (this == o) return true;
        if (!(o instanceof Village)) return false;

        Village village = (Village) o;

        if (!districtCode.equals(village.districtCode)) return false;
        if (!stateCode.equals(village.stateCode)) return false;
        if (!talukaCode.equals(village.talukaCode)) return false;
        if (!villageCode.equals(village.villageCode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stateCode.hashCode();
        result = 31 * result + districtCode.hashCode();
        result = 31 * result + talukaCode.hashCode();
        result = 31 * result + villageCode.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Village{" +
                "stateCode=" + stateCode +
                ", districtCode=" + districtCode +
                ", talukaCode=" + talukaCode +
                ", villageCode=" + villageCode +
                '}';
    }
}
