package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.event.CrudEventType;

import java.lang.Override;
import java.lang.String;

/**
 * Created by abhishek on 24/1/15.
 */
@Entity(
        recordHistory = true
)
@CrudEvents(
        CrudEventType.CREATE
)
public class DistrictCsv extends LocationUnitMetaDataCsv {

    @Field(name = "districtCode")
    private String districtCode;

    @Field(name = "stateId")
    private String stateCode;

    public DistrictCsv(String operation, String name,String districtCode,String stateCode) {
        super(operation, name);
        this.districtCode = districtCode;
        this.stateCode = stateCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    public String toString() {
        return "DistrictCsv{" +
                "districtCode=" + districtCode +
                ", stateCode=" + stateCode +
                '}';
    }
}
