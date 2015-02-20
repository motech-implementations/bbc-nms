package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;

@Entity
public class StateCapMappingCsv {


    @Field(required = true)
    @Unique
    Long index;

    @Field
    private String operation = "ADD";

    @Field
    private String stateId;

    @Field
    private String mobileKunjiCappingValue;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getMobileKunjiCappingValue() {
        return mobileKunjiCappingValue;
    }

    public void setMobileKunjiCappingValue(String mobileKunjiCappingValue) {
        this.mobileKunjiCappingValue = mobileKunjiCappingValue;
    }

    public boolean validateParameters() {
        /*validation to add*/
        return true;
    }
}
