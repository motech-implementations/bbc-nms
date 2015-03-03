package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Unique;

/**
 * This class Models data for LanguageLocationCode records
 */
@Entity(recordHistory = true)
@Unique(name = "state_district_id", members = {"state_id", "district_id"})
public class LanguageLocationCode extends MdsEntity {

    @Field(required = true, name = "state_id")
    @Cascade(persist = false, update = false)
    private State state;

    @Field(required = true)
    private Long stateCode;

    @Field(required = true, name = "district_id")
    @Cascade(persist = false, update = false)
    private District district;

    @Field(required = true)
    private Long districtCode;

    @Field(required = true, name = "circle_id")
    @Cascade(persist = false, update = false)
    private Circle circle;

    @Field(required = true)
    private String circleCode;

    @Field(required = true)
    private Integer languageLocationCode;

    @Field(required = true)
    private String languageMA;

    @Field(required = true)
    private String languageMK;

    @Field(required = true)
    private String languageKK;

    public LanguageLocationCode() {
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Integer getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(Integer languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public String getLanguageMA() {
        return languageMA;
    }

    public void setLanguageMA(String languageMA) {
        this.languageMA = languageMA;
    }

    public String getLanguageMK() {
        return languageMK;
    }

    public void setLanguageMK(String languageMK) {
        this.languageMK = languageMK;
    }

    public String getLanguageKK() {
        return languageKK;
    }

    public void setLanguageKK(String languageKK) {
        this.languageKK = languageKK;
    }

    public Long getStateCode() {
        return stateCode;
    }

    public void setStateCode(Long stateCode) {
        this.stateCode = stateCode;
    }

    public Long getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(Long districtCode) {
        this.districtCode = districtCode;
    }

    public String getCircleCode() {
        return circleCode;
    }

    public void setCircleCode(String circleCode) {
        this.circleCode = circleCode;
    }
}
