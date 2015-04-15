package org.motechproject.nms.kilkariobd.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import javax.jdo.annotations.Unique;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This entity represents the configuration parameters of Kilkariobd.
 */
@Entity(recordHistory = true)
public class Configuration {

    public static final Long CONFIGURATION_INDEX = 1L;

    @Unique
    @Field(required = true)
    @Min(1)
    @Max(1)
    @NotNull
    private Long index;

    @Field(required = true)
    private String freshObdServiceId;

    @Field(required = true)
    private String retryDay1ObdServiceId;

    @Field(required = true)
    private String retryDay2ObdServiceId;

    @Field(required = true)
    private String retryDay3ObdServiceId;

    @Field(required = true)
    private String freshObdPriority;

    @Field(required = true)
    private String retryDay1ObdPriority;

    @Field(required = true)
    private String retryDay2ObdPriority;

    @Field(required = true)
    private String retryDay3ObdPriority;

    @Field(required = true)
    private String obdFileLocalPath;


    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getFreshObdServiceId() {
        return freshObdServiceId;
    }

    public void setFreshObdServiceId(String freshObdServiceId) {
        this.freshObdServiceId = freshObdServiceId;
    }

    public String getRetryDay1ObdServiceId() {
        return retryDay1ObdServiceId;
    }

    public void setRetryDay1ObdServiceId(String retryDay1ObdServiceId) {
        this.retryDay1ObdServiceId = retryDay1ObdServiceId;
    }

    public String getRetryDay2ObdServiceId() {
        return retryDay2ObdServiceId;
    }

    public void setRetryDay2ObdServiceId(String retryDay2ObdServiceId) {
        this.retryDay2ObdServiceId = retryDay2ObdServiceId;
    }

    public String getRetryDay3ObdServiceId() {
        return retryDay3ObdServiceId;
    }

    public void setRetryDay3ObdServiceId(String retryDay3ObdServiceId) {
        this.retryDay3ObdServiceId = retryDay3ObdServiceId;
    }

    public String getFreshObdPriority() {
        return freshObdPriority;
    }

    public void setFreshObdPriority(String freshObdPriority) {
        this.freshObdPriority = freshObdPriority;
    }

    public String getRetryDay1ObdPriority() {
        return retryDay1ObdPriority;
    }

    public void setRetryDay1ObdPriority(String retryDay1ObdPriority) {
        this.retryDay1ObdPriority = retryDay1ObdPriority;
    }

    public String getRetryDay2ObdPriority() {
        return retryDay2ObdPriority;
    }

    public void setRetryDay2ObdPriority(String retryDay2ObdPriority) {
        this.retryDay2ObdPriority = retryDay2ObdPriority;
    }

    public String getRetryDay3ObdPriority() {
        return retryDay3ObdPriority;
    }

    public void setRetryDay3ObdPriority(String retryDay3ObdPriority) {
        this.retryDay3ObdPriority = retryDay3ObdPriority;
    }

    public String getObdFileLocalPath() {
        return obdFileLocalPath;
    }

    public void setObdFileLocalPath(String obdFileLocalPath) {
        this.obdFileLocalPath = obdFileLocalPath;
    }
}

