package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Persistent;
import java.util.List;

/**
 * Created by abhishek on 24/1/15.
 */
@Entity(recordHistory = true)
public class HealthFacility extends LocationUnitMetaData{

    @Field
    private int facilityType;

    @Field
    @Persistent(defaultFetchGroup = "true")
    @Column(name="phc_id")
    private List<HealthSubFacility> healthSubFacility;

    public HealthFacility(String name, int facilityType) {
        super(name);
        this.facilityType = facilityType;
    }

    public int getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(int facilityType) {
        this.facilityType = facilityType;
    }

    public List<HealthSubFacility> getHealthSubFacility() {
        return healthSubFacility;
    }

    public void setHealthSubFacility(List<HealthSubFacility> healthSubFacility) {
        this.healthSubFacility = healthSubFacility;
    }
}
