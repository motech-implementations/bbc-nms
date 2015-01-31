package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Created by abhishek on 24/1/15.
 */
@Entity(recordHistory = true)
public class Village extends LocationUnitMetaData {

    @Field
    private Long villageCode;

    @Field
    private String talukaCode;

    @Field
    private Long districtCode;

    @Field
    private Long stateCode;

    public Village(String name) {
        super(name);
    }
}
