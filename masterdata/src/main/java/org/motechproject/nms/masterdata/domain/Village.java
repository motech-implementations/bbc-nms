package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;

/**
 * Created by abhishek on 24/1/15.
 */
@Entity(recordHistory = true)
public class Village extends LocationUnitMetaData {

    public Village(String name) {
        super(name);
    }
}
