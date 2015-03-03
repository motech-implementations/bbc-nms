package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * This class Models data records provided in the State Csv Upload
 */

@Entity(
        recordHistory = true
)
public class StateCsv extends LocationUnitMetaDataCsv {

    @Field(name = "stateCode")
    private String stateCode;

    @Field(name = "maCapping")
    private String maCapping;

    @Field
    private String mkCapping;


    public StateCsv(String operation, String name, String stateCode, String maCapping, String mkCapping) {
        super(operation, name);
        this.stateCode = stateCode;
        this.maCapping = maCapping;
        this.mkCapping = mkCapping;
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

    @Override
    public String toString() {
        return "StateCsv{" +
                "stateCode='" + stateCode + '\'' +
                ", maCapping='" + maCapping + '\'' +
                ", mkCapping='" + mkCapping + '\'' +
                '}';
    }
}
