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
public class State extends LocationUnitMetaData{

    @Field
    private long stateId;

    @Field
    @Persistent(defaultFetchGroup = "true")
    @Column(name="state_id")
    private List<District> district;


    public State(String name, long stateId, List<District> district) {
        super(name);
        this.setStateId(stateId);
        this.district = district;
    }

    public List<District> getDistrict() {
        return district;
    }

    public void setDistrict(List<District> district) {
        this.district = district;
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        this.stateId = stateId;
    }


}
