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

    @Field
    private String stateCode;

    @Field(defaultValue = "-1")
    private String maCapping;

    @Field(defaultValue = "-1")
    private String mkCapping;


    public StateCsv(String name, String stateCode, String maCapping, String mkCapping) {
        super(name);
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
