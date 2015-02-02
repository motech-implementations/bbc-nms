package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * Created by abhishek on 24/1/15.
 */

@Entity
public class LocationUnitMetaData extends MdsEntity {

    @Field(name = "name")
    private String name;

    public LocationUnitMetaData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
