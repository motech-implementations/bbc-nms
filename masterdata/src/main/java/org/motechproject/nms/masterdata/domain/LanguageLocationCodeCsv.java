package org.motechproject.nms.masterdata.domain;

//import com.sun.org.glassfish.gmbal.DescriptorFields;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;
import java.lang.String;

@Entity
public class LanguageLocationCodeCsv {

    @Field(required = true)
    @Unique
    Long index;

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
    private String isDefaultLanguageLocationCodeMA;

    @Field
    private String languageLocationCodeMK;

    @Field
    private String languageMK;

    @Field
    private String isDefaultLanguageLocationCodeMK;

    @Field
    private String languageLocationCodeKK;

    @Field
    private String languageKK;

    @Field
    private String isDefaultLanguageLocationCodeKK;

    @Field
    private String isDeployedMA;

    @Field
    private String isDeployedMK;

    @Field
    private String isDeployedKK;

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public void setIsDeployedMA(String isDeployedMA) {
        this.isDeployedMA = isDeployedMA;
    }

    public void setIsDeployedMK(String isDeployedMK) {
        this.isDeployedMK = isDeployedMK;
    }

    public void setIsDeployedKK(String isDeployedKK) {
        this.isDeployedKK = isDeployedKK;
    }

    public String getIsDeployedMA() {
        return isDeployedMA;
    }

    public String getIsDeployedMK() {
        return isDeployedMK;
    }

    public String getIsDeployedKK() {
        return isDeployedKK;
    }

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

    public String getIsDefaultLanguageLocationCodeMA() {
        return isDefaultLanguageLocationCodeMA;
    }

    public void setIsDefaultLanguageLocationCodeMA(String isDefaultLanguageLocationCodeMA) {
        this.isDefaultLanguageLocationCodeMA = isDefaultLanguageLocationCodeMA;
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

    public String getIsDefaultLanguageLocationCodeMK() {
        return isDefaultLanguageLocationCodeMK;
    }

    public void setIsDefaultLanguageLocationCodeMK(String isDefaultLanguageLocationCodeMK) {
        this.isDefaultLanguageLocationCodeMK = isDefaultLanguageLocationCodeMK;
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

    public String getIsDefaultLanguageLocationCodeKK() {
        return isDefaultLanguageLocationCodeKK;
    }

    public void setIsDefaultLanguageLocationCodeKK(String isDefaultLanguageLocationCodeKK) {
        this.isDefaultLanguageLocationCodeKK = isDefaultLanguageLocationCodeKK;
    }


}
