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
public class TalukaCsv extends LocationUnitMetaDataCsv {

    @Field
    private String districtId;

    @Field
    private String stateId;

    @Field
    private String tCode;

    public TalukaCsv(String operation, String name, String districtId,String stateId,String tCode) {
        super(operation, name);
        this.districtId = districtId;
        this.stateId = stateId;
        this.tCode = tCode;
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

    public String gettCode() {
        return tCode;
    }

    public void settCode(String tCode) {
        this.tCode = tCode;
    }

    @Override
    public String toString() {
        return "TalukaCsv{" +
                ", districtId=" + districtId +
                ", stateId=" + stateId +
                ", tCode=" + tCode +
                '}';
    }
}
