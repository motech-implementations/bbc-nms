package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;


@Entity(recordHistory = true)
public class Circle extends MdsEntity {

    @Field(required = true)
    private String name;

    @Field(required = true)
    private String code;

    @Field
    private Integer defaultLanguageLocationCodeMA;

    @Field
    private Integer defaultLanguageLocationCodeMK;

    @Field
    private Integer defaultLanguageLocationCodeKK;

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

    public Integer getDefaultLanguageLocationCodeMA() {
        return defaultLanguageLocationCodeMA;
    }

    public void setDefaultLanguageLocationCodeMA(Integer defaultLanguageLocationCodeMA) {
        this.defaultLanguageLocationCodeMA = defaultLanguageLocationCodeMA;
    }

    public Integer getDefaultLanguageLocationCodeMK() {
        return defaultLanguageLocationCodeMK;
    }

    public void setDefaultLanguageLocationCodeMK(Integer defaultLanguageLocationCodeMK) {
        this.defaultLanguageLocationCodeMK = defaultLanguageLocationCodeMK;
    }

    public Integer getDefaultLanguageLocationCodeKK() {
        return defaultLanguageLocationCodeKK;
    }

    public void setDefaultLanguageLocationCodeKK(Integer defaultLanguageLocationCodeKK) {
        this.defaultLanguageLocationCodeKK = defaultLanguageLocationCodeKK;
    }
}
