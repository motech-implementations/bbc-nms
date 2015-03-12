package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.jdo.annotations.Persistent;
import java.util.Set;

/**
 * This class Models data for State location records
 */
@Entity(recordHistory = true)
public class State extends LocationUnitMetaData {

    @Field
    @UIDisplayable(position = 1)
    private Long stateCode;

    @Field
    @Cascade(delete = true)
    @Persistent(defaultFetchGroup = "true")
    private Set<District> district;

    @Field
    @UIDisplayable(position = 2)
    private Integer maCapping;

    @Field
    @UIDisplayable(position = 3)
    private Integer mkCapping;

    public State() {
    }

    public Long getStateCode() {
        return stateCode;
    }

    public void setStateCode(Long stateCode) {
        this.stateCode = stateCode;
    }

    public Set<District> getDistrict() {
        return district;
    }

    public void setDistrict(Set<District> district) {
        this.district = district;
    }

    public Integer getMkCapping() {
        return mkCapping;
    }

    public void setMkCapping(Integer mkCapping) {
        this.mkCapping = mkCapping;
    }

    public Integer getMaCapping() {
        return maCapping;
    }

    public void setMaCapping(Integer maCapping) {
        this.maCapping = maCapping;
    }

    @Override
    public String toString() {
        return "State{" +
                "stateCode=" + stateCode +
                ", district=" + district +
                ", mkCapping=" + mkCapping +
                ", maCapping=" + maCapping +
                '}';
    }
}