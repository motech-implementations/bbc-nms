package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import java.lang.String;

public class LanguageLocationCode extends MdsEntity{

    @Field(required = true)
    private Integer stateId;

    @Field(required = true)
    private Integer districtId;

    @Field(required = true)
    private String circleId;

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

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
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
