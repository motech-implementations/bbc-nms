package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;

import java.util.Set;

/**
 * This class Models data for State location records
 */
@Entity(recordHistory = true)
public class State extends MdsEntity {

    @Field
    @UIDisplayable(position = 0)
    private String name;

    @Field
    @UIDisplayable(position = 1)
    private Long stateCode;

    @Field
    @Cascade(delete = true)
    private Set<District> district;

    @Field
    @UIDisplayable(position = 2)
    private Integer maCapping;

    @Field
    @UIDisplayable(position = 3)
    private Integer mkCapping;


    @Field(defaultValue = "true")
    @UIDisplayable(position = 4)
    private Boolean isMkDeployed;

    @Field(defaultValue = "true")
    @UIDisplayable(position = 5)
    private Boolean isMaDeployed;

    @Field(defaultValue = "true")
    @UIDisplayable(position = 6)
    private Boolean isKkDeployed;

    @Field(defaultValue = "false")
    @UIDisplayable(position = 7)
    private Boolean isWhiteListEnable;


    public State() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStateCode() {
        return stateCode;
    }

    public void setStateCode(Long stateCode) {
        this.stateCode = stateCode;
    }

    public Set<District> getDistrict() {
        return district;
    }

    public void setDistrict(Set<District> district) {
        this.district = district;
    }

    public Integer getMkCapping() {
        return mkCapping;
    }

    public void setMkCapping(Integer mkCapping) {
        this.mkCapping = mkCapping;
    }

    public Integer getMaCapping() {
        return maCapping;
    }

    public void setMaCapping(Integer maCapping) {
        this.maCapping = maCapping;
    }


    public Boolean getIsMkDeployed() {
        return isMkDeployed;
    }

    public void setIsMkDeployed(Boolean isMkDeployed) {
        this.isMkDeployed = isMkDeployed;
    }

    public Boolean getIsMaDeployed() {
        return isMaDeployed;
    }

    public void setIsMaDeployed(Boolean isMaDeployed) {
        this.isMaDeployed = isMaDeployed;
    }

    public Boolean getIsKkDeployed() {
        return isKkDeployed;
    }

    public void setIsKkDeployed(Boolean isKkDeployed) {
        this.isKkDeployed = isKkDeployed;
    }

    public Boolean getIsWhiteListEnable() {
        return isWhiteListEnable;
    }

    public void setIsWhiteListEnable(Boolean isWhiteListEnable) {
        this.isWhiteListEnable = isWhiteListEnable;
    }

    /**
     * This method override the toString method to create string for name, state code
     * District, maCapping and mkCapping for the instance variables
     *
     * @return The string of the name, state code
     * District, maCapping and mkCapping  for the instance variables
     */
    @Override
    public String toString() {
        return "State{" +
                "name='" + name + '\'' +
                ", stateCode=" + stateCode +
                ", district=" + district +
                ", maCapping=" + maCapping +
                ", mkCapping=" + mkCapping +
                ", IsMkDeployed='" + isMkDeployed + '\'' +
                ", IsMaDeployed='" + isMaDeployed + '\'' +
                ", IsKkDeployed='" + isKkDeployed + '\'' +
                ", IsWhiteListEnable='" + isWhiteListEnable + '\'' +
                '}';
    }
}