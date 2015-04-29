package org.motechproject.nms.kilkariobd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import javax.jdo.annotations.Unique;

/**
 * This entity shall contains the status corresponding to the processing OBD target file and CDR files.
 */
@Entity
public class OutboundCallFlow {

    @UIDisplayable(position = 0)
    @Unique
    @Field
    private String obdFileName;

    @UIDisplayable(position = 1)
    @Field
    private CallFlowStatus status;

    @UIDisplayable(position = 2)
    @Field
    private String cdrSummaryChecksum;

    @UIDisplayable(position = 3)
    @Field
    private Long cdrSummaryRecordCount;

    @UIDisplayable(position = 4)
    @Field
    private String cdrDetailChecksum;

    @UIDisplayable(position = 5)
    @Field
    private Long cdrDetailRecordCount;

    @UIDisplayable(position = 6)
    @Field
    private String obdChecksum;

    @UIDisplayable(position = 7)
    @Field
    private Long obdRecordCount;

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

    public String getCdrDetailChecksum() {
        return cdrDetailChecksum;
    }

    public void setCdrDetailChecksum(String cdrDetailChecksum) {
        this.cdrDetailChecksum = cdrDetailChecksum;
    }

    public String getObdChecksum() {
        return obdChecksum;
    }

    public void setObdChecksum(String obdChecksum) {
        this.obdChecksum = obdChecksum;
    }

    public Long getCdrSummaryRecordCount() {
        return cdrSummaryRecordCount;
    }

    public void setCdrSummaryRecordCount(Long cdrSummaryRecordCount) {
        this.cdrSummaryRecordCount = cdrSummaryRecordCount;
    }

    public Long getCdrDetailRecordCount() {
        return cdrDetailRecordCount;
    }

    public void setCdrDetailRecordCount(Long cdrDetailRecordCount) {
        this.cdrDetailRecordCount = cdrDetailRecordCount;
    }

    public Long getObdRecordCount() {
        return obdRecordCount;
    }

    public void setObdRecordCount(Long obdRecordCount) {
        this.obdRecordCount = obdRecordCount;
    }
}
