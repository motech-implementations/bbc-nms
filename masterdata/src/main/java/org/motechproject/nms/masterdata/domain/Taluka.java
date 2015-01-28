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
public class Taluka extends LocationUnitMetaData {

    @Field
    private String tCode;

    @Field
    @Persistent(defaultFetchGroup = "true")
    @Column(name="taluka_id")
    private List<HealthBlock> healthBlock;

    @Field
    @Persistent(defaultFetchGroup = "true")
    @Column(name="village_id")
    private List<Village> village;

    public Taluka(String name, String tCode, List<HealthBlock> healthBlock, List<Village> village) {
        super(name);
        this.tCode = tCode;
        this.healthBlock = healthBlock;
        this.village = village;
    }

    public List<HealthBlock> getHealthBlock() {
        return healthBlock;
    }

    public void setHealthBlock(List<HealthBlock> healthBlock) {
        this.healthBlock = healthBlock;
    }


    public List<Village> getVillage() {
        return village;
    }

    public void setVillage(List<Village> village) {
        this.village = village;
    }

    public String gettCode() {
        return tCode;
    }

    public void settCode(String tCode) {
        this.tCode = tCode;
    }
}
