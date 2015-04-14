package org.motechproject.nms.kilkariobd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

@Entity
public class OutboundCallFlow {
    
    @Field
    private String obdFileName;
    
    @Field
    private String status;

    public String getObdFileName() {
        return obdFileName;
    }

    public void setObdFileName(String obdFileName) {
        this.obdFileName = obdFileName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
