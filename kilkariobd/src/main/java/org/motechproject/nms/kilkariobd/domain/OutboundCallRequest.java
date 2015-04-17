package org.motechproject.nms.kilkariobd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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
    private Integer priority;
    
    @Field
    private String callFlowURL;
    
    @Field(required = true)
    private String contentFileName;
    
    @Field(required = true)
    private String weekId;
    
    @Min(value=1)
    @Max(value=99)
    @Field(required = true)
    private Integer languageLocationCode;
    
    @Field(required = true)
    private String circleCode;
    
    @Field(required = true)
    private Integer retryDayNumber;

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

    public Integer getRetryDayNumber() {
        return retryDayNumber;
    }

    public void setRetryDayNumber(Integer retryDayNumber) {
        this.retryDayNumber = retryDayNumber;
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

    public Integer getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(Integer languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public String getCircleCode() {
        return circleCode;
    }

    public void setCircleCode(String circleCode) {
        this.circleCode = circleCode;
    }
    
}
