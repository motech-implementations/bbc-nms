package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Persistent;
import java.util.List;

/**
 * Created by abhishek on 24/1/15.
 */
@Entity(recordHistory = true)
public class District extends LocationUnitMetaData {

    @Field
    private Long districtId;

    @Field
    @Persistent(defaultFetchGroup = "true")
    @Column(name="district_id")
    private List<Taluka> taluka;

    public State getState_id() {
        return state_id;
    }

    public void setState_id(State state_id) {
        this.state_id = state_id;
    }

    @Field
    private State state_id;

    public District(String name, Long districtId, List<Taluka> taluka, State state_id) {
        super(name);
        this.districtId = districtId;
        this.taluka = taluka;
        this.state_id = state_id;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public List<Taluka> getTaluka() {
        return taluka;
    }

    public void setTaluka(List<Taluka> taluka) {
        this.taluka = taluka;
    }
}
