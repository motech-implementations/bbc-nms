package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * This class Models data records provided in the Taluka Csv Upload
 */
@Entity
public class TalukaCsv extends LocationUnitMetaDataCsv {

    @Field
    private String districtCode;

    @Field
    private String stateCode;

    @Field
    private String talukaCode;

    public TalukaCsv(String name, String districtCode, String stateCode, String talukaCode) {
        super(name);
        this.districtCode = districtCode;
        this.stateCode = stateCode;
        this.talukaCode = talukaCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getTalukaCode() {
        return talukaCode;
    }

    public void setTalukaCode(String tCode) {
        this.talukaCode = tCode;
    }

    /**
     * This method override the toString method to create string for state code
     * District code and taluka code for the instance variables
     *
     * @return The string of the state code
     * District code and taluka code for the instance variables
     */
    @Override
    public String toString() {
        return "TalukaCsv{" +
                ", districtId=" + districtCode +
                ", stateId=" + stateCode +
                ", talukaCode=" + talukaCode +
                '}';
    }

}
