package org.motechproject.nms.masterdata.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Persistent;
import java.util.List;

/**
 * Created by abhishek on 24/1/15.
 */
@Entity
public class District extends LocationUnitMetaData {

    @Field
    private long districtId;

    @Field
    @Persistent(defaultFetchGroup = "true")
    @Column(name="district_id")
    private List<Taluka> taluka;

    public District(String name, long districtId, List<Taluka> taluka) {
        super(name);
        this.districtId = districtId;
        this.taluka = taluka;
    }

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public List<Taluka> getTaluka() {
        return taluka;
    }

    public void setTaluka(List<Taluka> taluka) {
        this.taluka = taluka;
    }
}
