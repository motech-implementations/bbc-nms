package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data records provided in the District Csv Upload
 */

@Entity
public class DistrictCsv extends MdsEntity {

    @Field
    private String name;

    @Field
    private String districtCode;

    @Field
    private String stateCode;

    public DistrictCsv(String name, String districtCode, String stateCode) {
        this.name = name;
        this.districtCode = districtCode;
        this.stateCode = stateCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    /**
     * This method override the toString method to create string for District code and
     * State Code for the instance variables
     *
     * @return The string of the District code and State Code of the instance variables.
     */
    @Override
    public String toString() {
        return "DistrictCsv{" +
                "name='" + name + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                '}';
    }
}
