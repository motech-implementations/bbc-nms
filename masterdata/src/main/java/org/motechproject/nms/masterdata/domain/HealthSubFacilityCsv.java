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
public class HealthSubFacilityCsv extends LocationUnitMetaDataCsv{

    @Field
    private String sId;

    @Field
    private String tCode;

    @Field
    private String phcId;

    @Field
    private String stateId;

    @Field
    private String districtId;

    public HealthSubFacilityCsv(String operation, String name, String sId, String tCode, String stateId, String districtId, String phcId) {
        super(operation, name);
        this.sId = sId;
        this.tCode = tCode;
        this.stateId = stateId;
        this.districtId = districtId;
        this.phcId = phcId;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String gettCode() {
        return tCode;
    }

    public void settCode(String tCode) {
        this.tCode = tCode;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getPhcId() {
        return phcId;
    }

    public void setPhcId(String phcId) {
        this.phcId = phcId;
    }

    @Override
    public String toString() {
        return "HealthSubFacilityCsv{" +
                "sId=" + sId +
                ", tCode=" + tCode +
                ", phcId=" + phcId +
                ", stateId=" + stateId +
                ", districtId=" + districtId +
                '}';
    }
}
