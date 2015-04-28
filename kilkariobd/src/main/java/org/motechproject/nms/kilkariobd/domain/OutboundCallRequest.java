package org.motechproject.nms.kilkariobd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * This entity shall contains the OBD Call definition records, where each record shall have
 * parameters required by IVR to make call for OBD message delivery.
 */
@Entity
public class OutboundCallRequest {

    @Unique
    @Field(required = true)
    private String requestId;
    
    @Unique
    @Field(required = true)
    private String serviceId;
    
    @Field(required = true)
    private String msisdn;
    
    @Field
    private String cli;
    
    @Field
    private Integer priority = 0;
    
    @Field
    private String callFlowURL;
    
    @Field(required = true)
    private String contentFileName;
    
    @Field(required = true)
    private String weekId;
    
    @Min(value=1)
    @Max(value=99)
    @Field(required = true)
    private String languageLocationCode;
    
    @Field(required = true)
    private String circle;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCli() {
        return cli;
    }

    public void setCli(String cli) {
        this.cli = cli;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCallFlowURL() {
        return callFlowURL;
    }

    public void setCallFlowURL(String callFlowURL) {
        this.callFlowURL = callFlowURL;
    }

    public String getContentFileName() {
        return contentFileName;
    }

    public void setContentFileName(String contentFileName) {
        this.contentFileName = contentFileName;
    }

    public String getWeekId() {
        return weekId;
    }

    public void setWeekId(String weekId) {
        this.weekId = weekId;
    }

    public String getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(String languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }
}
