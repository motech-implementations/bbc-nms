package org.motechproject.nms.kilkariobd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;
import org.springframework.context.annotation.Profile;

@Entity
public class OutboundCallFlow extends MdsEntity {
    
    @Field
    private String obdFileName;
    
    @Field
    private CallFlowStatus status;

    @Field
    private String cdrSummaryChecksum;

    @Field
    private Integer cdrSummaryRecordCount;

    @Field
    private String cdrDetailChecksum;

    @Field
    private Integer cdrDetailRecordCount;

    @Field
    private String obdChecksum;

    @Field
    private Integer obdRecordCount;

    public String getObdFileName() {
        return obdFileName;
    }

    public void setObdFileName(String obdFileName) {
        this.obdFileName = obdFileName;
    }

    public CallFlowStatus getStatus() {
        return status;
    }

    public void setStatus(CallFlowStatus status) {
        this.status = status;
    }

    public String getCdrSummaryChecksum() {
        return cdrSummaryChecksum;
    }

    public void setCdrSummaryChecksum(String cdrSummaryChecksum) {
        this.cdrSummaryChecksum = cdrSummaryChecksum;
    }

    public Integer getCdrSummaryRecordCount() {
        return cdrSummaryRecordCount;
    }

    public void setCdrSummaryRecordCount(Integer cdrSummaryRecordCount) {
        this.cdrSummaryRecordCount = cdrSummaryRecordCount;
    }

    public String getCdrDetailChecksum() {
        return cdrDetailChecksum;
    }

    public void setCdrDetailChecksum(String cdrDetailChecksum) {
        this.cdrDetailChecksum = cdrDetailChecksum;
    }

    public Integer getCdrDetailRecordCount() {
        return cdrDetailRecordCount;
    }

    public void setCdrDetailRecordCount(Integer cdrDetailRecordCount) {
        this.cdrDetailRecordCount = cdrDetailRecordCount;
    }

    public String getObdChecksum() {
        return obdChecksum;
    }

    public void setObdChecksum(String obdChecksum) {
        this.obdChecksum = obdChecksum;
    }

    public Integer getObdRecordCount() {
        return obdRecordCount;
    }

    public void setObdRecordCount(Integer obdRecordCount) {
        this.obdRecordCount = obdRecordCount;
    }
}
