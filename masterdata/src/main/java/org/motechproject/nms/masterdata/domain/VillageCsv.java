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
public class VillageCsv extends LocationUnitMetaDataCsv{

    @Field
    private String villageId;

    @Field
    private String stateId;

    @Field
    private String districtId;

    @Field
    private String tCode;

    public VillageCsv(String operation, String name,String villageId,String stateId,String districtId,String tCode) {
        super(operation, name);
        this.villageId = villageId;
        this.stateId = stateId;
        this.districtId =districtId;
        this.tCode = tCode;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
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
        return "VillageCsv{" +
                "villageId=" + villageId +
                ", stateId=" + stateId +
                ", districtId=" + districtId +
                ", tCode=" + tCode +
                '}';
    }
}
