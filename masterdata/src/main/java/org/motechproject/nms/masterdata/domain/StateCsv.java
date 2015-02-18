package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Created by abhishek on 24/1/15.
 */

@Entity(
        recordHistory = true
)
public class StateCsv extends LocationUnitMetaDataCsv {

    @Field(name = "stateid")
    private String stateId;

    public StateCsv(String operation, String name, String stateid) {
        super(operation, name);
        this.setStateId(getStateId());
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }
}
