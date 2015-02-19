package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class MobileKunjiStateCapMapping {


    @Field(required = true)
    private Long stateId;

    @Field(required = true)
    private int mobileKunjiCappingValue;

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public int getMobileKunjiCappingValue() {
        return mobileKunjiCappingValue;
    }

    public void setMobileKunjiCappingValue(int mobileKunjiCappingValue) {
        this.mobileKunjiCappingValue = mobileKunjiCappingValue;
    }
}
