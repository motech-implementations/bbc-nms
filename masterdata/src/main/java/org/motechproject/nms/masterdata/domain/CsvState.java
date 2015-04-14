package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data records provided in the State Csv Upload
 */

@Entity
public class CsvState extends MdsEntity {

    @Field
    private String name;

    @Field
    private String stateCode;

    @Field(defaultValue = "-1")
    private String maCapping;

    @Field(defaultValue = "-1")
    private String mkCapping;

    @Field(defaultValue = "true")
    private String isMkDeployed;


    @Field(defaultValue = "true")
    private String isMaDeployed;

    @Field(defaultValue = "true")
    private String isKkDeployed;

    @Field(defaultValue = "true")
    private String isWhiteListEnable;

    public CsvState(String name, String stateCode, String maCapping, String mkCapping, String isMkDeployed, String isMaDeployed, String isKkDeployed, String isWhiteListEnable) {
        this.name = name;
        this.stateCode = stateCode;
        this.maCapping = maCapping;
        this.mkCapping = mkCapping;
        this.isMkDeployed = isMkDeployed;
        this.isMaDeployed = isMaDeployed;
        this.isKkDeployed = isKkDeployed;
        this.isWhiteListEnable = isWhiteListEnable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getMaCapping() {
        return maCapping;
    }

    public void setMaCapping(String maCapping) {
        this.maCapping = maCapping;
    }

    public String getMkCapping() {
        return mkCapping;
    }

    public void setMkCapping(String mkCapping) {
        this.mkCapping = mkCapping;
    }

    public String getIsWhiteListEnable() {
        return isWhiteListEnable;
    }

    public void setIsWhiteListEnable(String isWhiteListEnable) {
        this.isWhiteListEnable = isWhiteListEnable;
    }

    public String getIsMkDeployed() {
        return isMkDeployed;
    }

    public void setIsMkDeployed(String isMkDeployed) {
        this.isMkDeployed = isMkDeployed;
    }

    public String getIsMaDeployed() {
        return isMaDeployed;
    }

    public void setIsMaDeployed(String isMaDeployed) {
        this.isMaDeployed = isMaDeployed;
    }

    public String getIsKkDeployed() {
        return isKkDeployed;
    }

    public void setIsKkDeployed(String isKkDeployed) {
        this.isKkDeployed = isKkDeployed;
    }

    /**
     * This method override the toString method to create string for state code,
     * isMkDeployed, isMaDeployed, isKkDeployed, isWhiteListEnable, maCapping and mkCapping for the instance variables
     *
     * @return The string of the state code,isMkDeployed, isMaDeployed, isKkDeployed,
     * isWhiteListEnable, maCapping and mkCapping  for the instance variables
     */
    @Override
    public String toString() {
        return "StateCsv{" +
                "name='" + name + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", maCapping='" + maCapping + '\'' +
                ", mkCapping='" + mkCapping + '\'' +
                ", isMkDeployed='" + isMkDeployed + '\'' +
                ", isMaDeployed='" + isMaDeployed + '\'' +
                ", isKkDeployed='" + isKkDeployed + '\'' +
                ", isWhiteListEnable='" + isWhiteListEnable + '\'' +
                '}';
    }


}
