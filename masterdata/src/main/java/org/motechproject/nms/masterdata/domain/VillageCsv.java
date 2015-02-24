package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data records provided in the Village Csv Upload
 */
@Entity
public class VillageCsv extends MdsEntity {

    @Field
    private String name;

    @Field
    private String villageCode;

    @Field
    private String stateCode;

    @Field
    private String districtCode;

    @Field
    private String talukaCode;

    public VillageCsv(String name, String villageCode, String stateCode, String districtCode, String talukaCode) {
        this.name = name;
        this.villageCode = villageCode;
        this.stateCode = stateCode;
        this.districtCode = districtCode;
        this.talukaCode = talukaCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVillageCode() {
        return villageCode;
    }

    public void setVillageCode(String villageCode) {
        this.villageCode = villageCode;
    }

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

    public String getTalukaCode() {
        return talukaCode;
    }

    public void setTalukaCode(String talukaCode) {
        this.talukaCode = talukaCode;
    }

    /**
     * This method override the toString method to create string for state code
     * District code, taluka code and village code for the instance variables
     *
     * @return The string of the name, state code
     * District code, taluka code and village code for the instance variables
     */
    @Override
    public String toString() {
        return "VillageCsv{" +
                "name='" + name + '\'' +
                ", villageCode='" + villageCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", talukaCode='" + talukaCode + '\'' +
                '}';
    }
}
