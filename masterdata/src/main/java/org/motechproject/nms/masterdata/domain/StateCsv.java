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

    public StateCsv(String operation, String name, String stateCode) {

        super(operation, name);
        this.stateCode = stateCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    public String toString() {
        return "StateCsv{" +
                "stateCode=" + stateCode +
                '}';
    }
}
