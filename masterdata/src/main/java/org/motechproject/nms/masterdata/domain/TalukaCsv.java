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
    private String districtCode;

    @Field
    private String stateCode;

    @Field
    private String talukaCode;

    public TalukaCsv(String operation, String name, String districtCode, String stateCode, String talukaCode) {
        super(operation, name);
        this.districtCode = districtCode;
        this.stateCode = stateCode;
        this.talukaCode = talukaCode;
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

    public String getTalukaCode() {
        return talukaCode;
    }

    public void setTalukaCode(String tCode) {
        this.talukaCode = tCode;
    }

    @Override
    public String toString() {
        return "TalukaCsv{" +
                ", districtId=" + districtCode +
                ", stateId=" + stateCode +
                ", talukaCode=" + talukaCode +
                '}';
    }

}
