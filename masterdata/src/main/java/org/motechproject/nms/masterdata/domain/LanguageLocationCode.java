package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.lang.String;

@Entity
public class LanguageLocationCode {

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
    private boolean isDefaultLanguageLocationCodeMA;

    @Field(required = true)
    private Integer languageLocationCodeMK;

    @Field(required = true)
    private String languageMK;

    @Field(required = true)
    private boolean isDefaultLanguageLocationCodeMK;

    @Field(required = true)
    private Integer languageLocationCodeKK;

    @Field(required = true)
    private String languageKK;

    @Field(required = true)
    private boolean isDefaultLanguageLocationCodeKK;

    @Field(required = true)
    private String isDeployedMA;

    @Field(required = true)
    private String isDeployedMK;

    @Field(required = true)
    private String isDeployedKK;


    public LanguageLocationCode(){}

    public LanguageLocationCode(Integer stateId, Integer districtId, String circleId) {
        this.stateId = stateId;
        this.districtId = districtId;
        this.circleId = circleId;
    }


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

    public boolean isDefaultLanguageLocationCodeMA() {
        return isDefaultLanguageLocationCodeMA;
    }

    public void setDefaultLanguageLocationCodeMA(boolean isDefaultLanguageLocationCodeMA) {
        this.isDefaultLanguageLocationCodeMA = isDefaultLanguageLocationCodeMA;
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

    public boolean isDefaultLanguageLocationCodeMK() {
        return isDefaultLanguageLocationCodeMK;
    }

    public void setDefaultLanguageLocationCodeMK(boolean isDefaultLanguageLocationCodeMK) {
        this.isDefaultLanguageLocationCodeMK = isDefaultLanguageLocationCodeMK;
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

    public String getIsDeployedMA() {
        return isDeployedMA;
    }

    public void setIsDeployedMA(String isDeployedMA) {
        this.isDeployedMA = isDeployedMA;
    }

    public String getIsDeployedMK() {
        return isDeployedMK;
    }

    public void setIsDeployedMK(String isDeployedMK) {
        this.isDeployedMK = isDeployedMK;
    }

    public String getIsDeployedKK() {
        return isDeployedKK;
    }

    public void setIsDeployedKK(String isDeployedKK) {
        this.isDeployedKK = isDeployedKK;
    }

    public boolean isDefaultLanguageLocationCodeKK() {
        return isDefaultLanguageLocationCodeKK;
    }

    public void setDefaultLanguageLocationCodeKK(boolean isDefaultLanguageLocationCodeKK) {
        this.isDefaultLanguageLocationCodeKK = isDefaultLanguageLocationCodeKK;
    }
}
