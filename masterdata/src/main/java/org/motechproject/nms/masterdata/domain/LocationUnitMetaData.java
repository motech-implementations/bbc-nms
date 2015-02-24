package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class acts as parent class other masterdata classes
 */
@Entity
public class LocationUnitMetaData extends MdsEntity {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Field(name = "name")
    private String name;

    public LocationUnitMetaData() {

    }

}
