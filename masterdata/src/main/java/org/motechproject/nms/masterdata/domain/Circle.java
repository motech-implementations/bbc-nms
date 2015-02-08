package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import javax.jdo.annotations.Unique;

/**
 * This class Models data for Circle location records
 */

@Entity(recordHistory = true)
public class Circle extends MdsEntity {

    @Field(required = true)
    private String name;

    @Unique
    @Field(required = true)
    private String code;

    @Field
    private Integer defaultLanguageLocationCode;

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

    public Integer getDefaultLanguageLocationCode() {
        return defaultLanguageLocationCode;
    }

    public void setDefaultLanguageLocationCode(Integer defaultLanguageLocationCode) {
        this.defaultLanguageLocationCode = defaultLanguageLocationCode;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", defaultLanguageLocationCode=" + defaultLanguageLocationCode +
                '}';
    }
}
