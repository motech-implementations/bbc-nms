package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Created by abhishek on 24/1/15.
 */
@Entity
public class DistrictCsv extends LocationUnitMetaDataCsv {

    @Field(name = "districtId")
    private String districtId;

    @Field(name = "stateId")
    private String stateId;

    public DistrictCsv(String operation, String name,String districtId,String stateId) {
        super(operation, name);
        this.districtId = districtId;
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }
}
