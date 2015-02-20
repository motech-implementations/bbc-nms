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
    private Long stateId;

    @Field
    @Column(name ="state_id")
    @Persistent(defaultFetchGroup = "true",mappedBy = "state_id")
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

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }


}
