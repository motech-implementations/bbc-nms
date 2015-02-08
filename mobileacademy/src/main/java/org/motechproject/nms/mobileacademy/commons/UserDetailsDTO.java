package org.motechproject.nms.mobileacademy.commons;

import java.io.Serializable;

/**
 * User Details DTO object refer user details(i.e creator, owner, modifiedBy)
 * that used in add/update operations
 *
 */
public class UserDetailsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String owner;

    private String creator;

    private String modifiedBy;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

}
