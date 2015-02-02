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
public class HealthBlock extends LocationUnitMetaData {

    @Field
    private long healthBlockId;

    @Field
    @Persistent(defaultFetchGroup = "true")
    @Column(name="healthBlock_id")
    private List<HealthFacility> healthBlock;

    public HealthBlock(String name, long healthBlockId) {
        super(name);
        this.healthBlockId = healthBlockId;
    }

    public long getHealthBlockId() {
        return healthBlockId;
    }

    public void setHealthBlockId(long healthBlockId) {
        this.healthBlockId = healthBlockId;
    }

    public List<HealthFacility> getHealthBlock() {
        return healthBlock;
    }

    public void setHealthBlock(List<HealthFacility> healthBlock) {
        this.healthBlock = healthBlock;
    }
}