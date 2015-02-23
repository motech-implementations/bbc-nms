package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

public class CircleCsv extends MdsEntity {

    @Field
    private String operation;

    @Field
    private String name;

    @Field
    private String code;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    /**
     * This method override the toString method to create string for name and
     * value for the instance variables
     *
     * @return The string of the name and value of the instance variables.
     */
    public String toString() {
        StringBuffer recordStr = new StringBuffer();
        recordStr.append("name" + this.name).append(",code" + this.code);

        return recordStr.toString();

    }
}
