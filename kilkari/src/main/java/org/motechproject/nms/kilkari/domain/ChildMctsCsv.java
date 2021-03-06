package org.motechproject.nms.kilkari.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * This entity represents the child mcts record.
 */
@Entity
public class ChildMctsCsv extends MctsCsv {
    
    @Field
    private String motherName;

    @Field
    private String motherId;

    @Field
    private String birthdate;

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "Mcts Id["+this.getIdNo()+"] stateCode["+this.getStateCode()+"]";
    }

}
