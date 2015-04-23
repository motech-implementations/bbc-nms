package org.motechproject.nms.frontlineworker.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data records provided in the White List Users Csv Upload
 */
@Entity
public class CsvWhiteListUsers extends MdsEntity {

    @Field
    private String contactNo;

    public CsvWhiteListUsers() {
    }

    public CsvWhiteListUsers(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    @Override
    public String toString() {
        return "Contact No[" + this.getContactNo() + "]";
    }
}
