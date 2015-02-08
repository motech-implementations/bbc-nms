package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class acts as parent class other masterdata classes
 */
@Entity
public class LocationUnitMetaData extends MdsEntity {

    @Field(name = "name")
    @UIDisplayable(position = 0)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LocationUnitMetaData{" +
                "name='" + name + '\'' +
                '}';
    }
}
