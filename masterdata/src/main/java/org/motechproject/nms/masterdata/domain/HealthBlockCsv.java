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
public class HealthBlockCsv extends LocationUnitMetaDataCsv {

    @Field
    private String healthBlockId;

    @Field
    private String stateId;

    @Field
    private String dCode;

    @Field
    private String tCode;


    public HealthBlockCsv(String operation, String name,String healthBlockId,String stateId,String dCode,String tCode) {
        super(operation, name);
        this.healthBlockId = healthBlockId;
        this.stateId = stateId;
        this.dCode = dCode;
        this.tCode = tCode;
    }

    public String getHealthBlockId() {
        return healthBlockId;
    }

    public void setHealthBlockId(String healthBlockId) {
        this.healthBlockId = healthBlockId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode;
    }

    public String gettCode() {
        return tCode;
    }

    public void settCode(String tCode) {
        this.tCode = tCode;
    }

    @Override
    public String toString() {
        return "HealthBlockCsv{" +
                "healthBlockId=" + healthBlockId +
                ", stateId=" + stateId +
                ", dCode=" + dCode +
                ", tCode=" + tCode +
                '}';
    }
}
