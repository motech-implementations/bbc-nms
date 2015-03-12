package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Unique;

/**
 * This class Models data for Operator records
 */
@Entity(recordHistory = true)
public class Operator extends MdsEntity {

    @Field(required = true)
    private String name;

    @Unique
    @Field(required = true)
    private String code;

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
     * This method override the toString method to create string for name and code the instance variables
     *
     * @return The string of the name and code for the instance variables
     */
    @Override
    public String toString() {
        return "Operator{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
