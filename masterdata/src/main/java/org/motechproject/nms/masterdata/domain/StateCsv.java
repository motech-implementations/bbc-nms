package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.event.CrudEventType;

/**
 * Created by abhishek on 24/1/15.
 */

@Entity
@CrudEvents(CrudEventType.CREATE)
public class StateCsv extends LocationUnitMetaDataCsv {

    @Field
    private String stateId;

    public StateCsv(String operation, String name, String stateId) {
        super(operation, name);
        this.stateId = stateId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }
}
