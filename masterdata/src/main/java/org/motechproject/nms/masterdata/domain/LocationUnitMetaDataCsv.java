package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

import java.lang.Override;
import java.lang.String;

/**
 * Created by abhishek on 18/1/15.
 */
@Entity
public class LocationUnitMetaDataCsv extends MdsEntity{

    @Field
    private String operation;

    @Field
    private String name;

    public LocationUnitMetaDataCsv(String operation, String name) {
        this.operation = operation;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "LocationUnitMetaDataCsv{" +
                "operation=" + operation +
                ", name=" + name +
                '}';
    }
}
