package org.motechproject.nms.masterdata.domain;


import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;

@Entity
public class OperatorCsv {

    @Field
    private String operation = "ADD";

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

    public String toString() {

        StringBuffer recordStr = new StringBuffer();
        recordStr.append("name" + this.name);

        recordStr.append(",code" + this.code);
        return recordStr.toString();

    }
}
