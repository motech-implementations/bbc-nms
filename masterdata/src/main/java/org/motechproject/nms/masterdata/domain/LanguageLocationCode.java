package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import java.lang.String;

public class LanguageLocationCode extends MdsEntity{

    @Field(required = true, name="state_id" )
    private State stateId;

    @Field(required=true)
    private Integer stateCode;

    @Field(required = true, name="district_id")
    private District districtId;

    @Field(required=true)
    private Integer stateCode;

    @Field(required = true, name="circle_id")
    private Circle circleId;

    @Field(required=true)
    private String circleCode;

    @Field(required = true)
    private Integer languageLocationCodeMA;

    @Field(required = true)
    private String languageMA;

    @Field(required = true)
    private Integer DefaultLanguageLocationCodeMA;

    @Field(required = true)
    private Integer languageLocationCodeMK;

    @Field(required = true)
    private String languageMK;

    @Field(required = true)
    private Integer DefaultLanguageLocationCodeMK;

    @Field(required = true)
    private Integer languageLocationCodeKK;

    @Field(required = true)
    private String languageKK;

    @Field(required = true)
    private Integer DefaultLanguageLocationCodeKK;

    public LanguageLocationCode(){}

    public State getStateId() {
        return stateId;
    }

    public void setStateId(State stateId) {
        this.stateId = stateId;
    }

    public District getDistrictId() {
        return districtId;
    }

    public void setDistrictId(District districtId) {
        this.districtId = districtId;
    }

    public Circle getCircleId() {
        return circleId;
    }

    public void setCircleId(Circle circleId) {
        this.circleId = circleId;
    }

    public Integer getLanguageLocationCodeMA() {
        return languageLocationCodeMA;
    }

    public void setLanguageLocationCodeMA(Integer languageLocationCodeMA) {
        this.languageLocationCodeMA = languageLocationCodeMA;
    }

    public String getLanguageMA() {
        return languageMA;
    }

    public void setLanguageMA(String languageMA) {
        this.languageMA = languageMA;
    }

    public Integer getLanguageLocationCodeMK() {
        return languageLocationCodeMK;
    }

    public void setLanguageLocationCodeMK(Integer languageLocationCodeMK) {
        this.languageLocationCodeMK = languageLocationCodeMK;
    }

    public String getLanguageMK() {
        return languageMK;
    }

    public void setLanguageMK(String languageMK) {
        this.languageMK = languageMK;
    }

    public Integer getLanguageLocationCodeKK() {
        return languageLocationCodeKK;
    }

    public void setLanguageLocationCodeKK(Integer languageLocationCodeKK) {
        this.languageLocationCodeKK = languageLocationCodeKK;
    }

    public String getLanguageKK() {
        return languageKK;
    }

    public void setLanguageKK(String languageKK) {
        this.languageKK = languageKK;
    }

    public Integer getDefaultLanguageLocationCodeMA() {
        return DefaultLanguageLocationCodeMA;
    }

    public void setDefaultLanguageLocationCodeMA(Integer defaultLanguageLocationCodeMA) {
        DefaultLanguageLocationCodeMA = defaultLanguageLocationCodeMA;
    }

    public Integer getDefaultLanguageLocationCodeMK() {
        return DefaultLanguageLocationCodeMK;
    }

    public void setDefaultLanguageLocationCodeMK(Integer defaultLanguageLocationCodeMK) {
        DefaultLanguageLocationCodeMK = defaultLanguageLocationCodeMK;
    }

    public Integer getDefaultLanguageLocationCodeKK() {
        return DefaultLanguageLocationCodeKK;
    }

    public void setDefaultLanguageLocationCodeKK(Integer defaultLanguageLocationCodeKK) {
        DefaultLanguageLocationCodeKK = defaultLanguageLocationCodeKK;
    }
}
