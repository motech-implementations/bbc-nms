package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Unique;
import java.lang.String;

public class LanguageLocationCodeCsv extends MdsEntity{

    @Field
    private String operation = "ADD";

    @Field
    private String stateId;

    @Field
    private String districtId;

    @Field
    private String circleId;

    @Field
    private String languageLocationCodeMA;

    @Field
    private String languageMA;

    @Field
    private String DefaultLanguageLocationCodeMA;

    @Field
    private String languageLocationCodeMK;

    @Field
    private String languageMK;

    @Field
    private String DefaultLanguageLocationCodeMK;

    @Field
    private String languageLocationCodeKK;

    @Field
    private String languageKK;

    @Field
    private String DefaultLanguageLocationCodeKK;

    public LanguageLocationCodeCsv(String operation, String stateId, String districtId, String circleId) {
        this.operation = operation;
        this.stateId = stateId;
        this.districtId = districtId;
        this.circleId = circleId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public String getLanguageLocationCodeMA() {
        return languageLocationCodeMA;
    }

    public void setLanguageLocationCodeMA(String languageLocationCodeMA) {
        this.languageLocationCodeMA = languageLocationCodeMA;
    }

    public String getLanguageMA() {
        return languageMA;
    }

    public void setLanguageMA(String languageMA) {
        this.languageMA = languageMA;
    }

    public String getLanguageLocationCodeMK() {
        return languageLocationCodeMK;
    }

    public void setLanguageLocationCodeMK(String languageLocationCodeMK) {
        this.languageLocationCodeMK = languageLocationCodeMK;
    }

    public String getLanguageMK() {
        return languageMK;
    }

    public void setLanguageMK(String languageMK) {
        this.languageMK = languageMK;
    }

    public String getLanguageLocationCodeKK() {
        return languageLocationCodeKK;
    }

    public void setLanguageLocationCodeKK(String languageLocationCodeKK) {
        this.languageLocationCodeKK = languageLocationCodeKK;
    }

    public String getLanguageKK() {
        return languageKK;
    }

    public void setLanguageKK(String languageKK) {
        this.languageKK = languageKK;
    }

    public String getDefaultLanguageLocationCodeMA() {
        return DefaultLanguageLocationCodeMA;
    }

    public void setDefaultLanguageLocationCodeMA(String defaultLanguageLocationCodeMA) {
        DefaultLanguageLocationCodeMA = defaultLanguageLocationCodeMA;
    }

    public String getDefaultLanguageLocationCodeKK() {
        return DefaultLanguageLocationCodeKK;
    }

    public void setDefaultLanguageLocationCodeKK(String defaultLanguageLocationCodeKK) {
        DefaultLanguageLocationCodeKK = defaultLanguageLocationCodeKK;
    }

    public String getDefaultLanguageLocationCodeMK() {
        return DefaultLanguageLocationCodeMK;
    }

    public void setDefaultLanguageLocationCodeMK(String defaultLanguageLocationCodeMK) {
        DefaultLanguageLocationCodeMK = defaultLanguageLocationCodeMK;
    }

    public String toString() {

        StringBuffer recordStr = new StringBuffer();
        recordStr.append("stateId" + this.stateId);

        recordStr.append(",districtId" + this.districtId);
        recordStr.append(",circleId" + this.circleId);
        recordStr.append(",languageLocationCodeMA" + this.languageLocationCodeMA);
        recordStr.append(",languageMA" + this.languageMA);
        recordStr.append(",isDefaultLanguageLocationCodeMA" + this.DefaultLanguageLocationCodeMA);
        recordStr.append(",languageLocationCodeMK" + this.languageLocationCodeMK);
        recordStr.append(",languageMK" + this.languageMK);
        recordStr.append(",isDefaultLanguageLocationCodeMK" + this.DefaultLanguageLocationCodeMK);
        recordStr.append(",languageLocationCodeKK" + this.languageLocationCodeKK);
        recordStr.append(",isDefaultLanguageLocationCodeKK" + this.DefaultLanguageLocationCodeKK);
        return recordStr.toString();

    }


}
