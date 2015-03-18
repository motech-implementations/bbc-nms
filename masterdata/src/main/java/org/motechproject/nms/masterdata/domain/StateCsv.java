package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data records provided in the State Csv Upload
 */

@Entity
public class StateCsv extends MdsEntity {

    @Field
    private String name;

    @Field
    private String stateCode;

    @Field(defaultValue = "-1")
    private String maCapping;

    @Field(defaultValue = "-1")
    private String mkCapping;


    public StateCsv(String name, String stateCode, String maCapping, String mkCapping) {
        this.name = name;
        this.stateCode = stateCode;
        this.maCapping = maCapping;
        this.mkCapping = mkCapping;
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

    /**
     * This method override the toString method to create string for state code
     * District, maCapping and mkCapping for the instance variables
     *
     * @return The string of the state code
     * District, maCapping and mkCapping  for the instance variables
     */
    @Override
    public String toString() {
        return "StateCsv{" +
                "name='" + name + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", maCapping='" + maCapping + '\'' +
                ", mkCapping='" + mkCapping + '\'' +
                '}';
    }
}
