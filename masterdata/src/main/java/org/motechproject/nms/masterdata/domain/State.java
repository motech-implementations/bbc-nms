package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Persistent;
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
    @Persistent(defaultFetchGroup = "true")
    private Set<District> district;

    @Field
    @UIDisplayable(position = 2)
    private Integer maCapping;

    @Field
    @UIDisplayable(position = 3)
    private Integer mkCapping;


    @Field
    @UIDisplayable(position = 4)
    private String IsMkDeployed;

    @Field
    @UIDisplayable(position = 5)
    private String IsMaDeployed;

    @Field
    @UIDisplayable(position = 6)
    private String IsKkDeployed;

    @Field
    @UIDisplayable(position = 7)
    private String IsWhiteListEnable;


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

    public String getIsWhiteListEnable() {
        return IsWhiteListEnable;
    }

    public void setIsWhiteListEnable(String isWhiteListEnable) {
        IsWhiteListEnable = isWhiteListEnable;
    }

    public String getIsMaDeployed() {
        return IsMaDeployed;
    }

    public void setIsMaDeployed(String isMaDeployed) {
        IsMaDeployed = isMaDeployed;
    }

    public String getIsMkDeployed() {
        return IsMkDeployed;
    }

    public void setIsMkDeployed(String isMkDeployed) {
        IsMkDeployed = isMkDeployed;
    }

    public String getIsKkDeployed() {
        return IsKkDeployed;
    }

    public void setIsKkDeployed(String isKkDeployed) {
        IsKkDeployed = isKkDeployed;
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
                ", IsMkDeployed='" + IsMkDeployed + '\'' +
                ", IsMaDeployed='" + IsMaDeployed + '\'' +
                ", IsKkDeployed='" + IsKkDeployed + '\'' +
                ", IsWhiteListEnable='" + IsWhiteListEnable + '\'' +
                '}';
    }
}