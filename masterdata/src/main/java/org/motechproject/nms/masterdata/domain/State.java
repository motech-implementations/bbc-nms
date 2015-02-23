package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Set;

/**
 * Created by abhishek on 24/1/15.
 */
@Entity(recordHistory = true)
public class State extends LocationUnitMetaData {

    @Field
    private Long stateCode;

    @Field
    private Set<District> district;

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

    @Override
    public String toString() {
        return "State{" +
                "stateCode=" + stateCode +
                ", district=" + district +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof State)) {
            return false;
        }

        State state = (State) o;

        if (!state.getStateCode().equals(state.getStateCode())) {
            return false;
        }

        return true;

    }
}