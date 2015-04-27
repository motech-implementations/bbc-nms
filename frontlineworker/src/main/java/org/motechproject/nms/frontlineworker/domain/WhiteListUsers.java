package org.motechproject.nms.frontlineworker.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data for WhiteListUsers records
 */


@Entity(recordHistory = true)
public class WhiteListUsers extends MdsEntity {

    @Field(required = true)
    private String contactNo;

    public WhiteListUsers() {
    }

    public WhiteListUsers(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }
}
