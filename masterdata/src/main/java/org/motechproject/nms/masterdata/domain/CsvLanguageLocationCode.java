package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data records provided in the LanguageLocationCode Csv Upload
 */

@Entity
public class CsvLanguageLocationCode extends MdsEntity {

    @Field
    private String stateCode;

    @Field
    private String districtCode;

    @Field
    private String circleCode;

    @Field
    private String languageLocationCode;

    @Field
    private String isDefaultLanguageLocationCode;

    @Field
    private String languageMA;

    @Field
    private String languageMK;

    @Field
    private String languageKK;

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getCircleCode() {
        return circleCode;
    }

    public void setCircleCode(String circleCode) {
        this.circleCode = circleCode;
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

    public String getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(String languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public String getIsDefaultLanguageLocationCode() {
        return isDefaultLanguageLocationCode;
    }

    public void setIsDefaultLanguageLocationCode(String isDefaultLanguageLocationCode) {
        this.isDefaultLanguageLocationCode = isDefaultLanguageLocationCode;
    }

    /**
     * This method override the toString method to create string for State Code,
     * District Code, Circle Code, Language Location Code, Language MA and
     * Language MK for the instance variables
     *
     * @return The string of the State Code,
     * District Code, Circle Code, Language Location Code, Language MA and
     * Language MK for the instance variables
     */
    public String toString() {

        StringBuffer recordStr = new StringBuffer();
        recordStr.append("stateCode [" + this.stateCode);
        recordStr.append("] districtCode [" + this.districtCode);
        recordStr.append("] circleCode [" + this.circleCode);
        recordStr.append("]languageLocationCode [" + this.languageLocationCode);
        recordStr.append("] languageMA [" + this.languageMA);
        recordStr.append("] languageMK [" + this.languageMK);
        recordStr.append("] languageKK [" + this.languageKK);
        recordStr.append("] isDefaultLanguageLocationCode [" + this.isDefaultLanguageLocationCode);
        return recordStr.toString();

    }


}
