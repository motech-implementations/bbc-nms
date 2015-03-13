package org.motechproject.nms.kilkari.domain;

import javax.jdo.annotations.Unique;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.motechproject.mds.annotations.Field;

public class ActiveUser {

    @Unique
    @Field(required = true)
    @Min(1)
    @Max(1)
    @NotNull
    private Long index;
    
    @Field(required = true)
    private Long activeUserCount;

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Long getActiveUserCount() {
        return activeUserCount;
    }

    public void setActiveUserCount(Long activeUserCount) {
        this.activeUserCount = activeUserCount;
    }

}
