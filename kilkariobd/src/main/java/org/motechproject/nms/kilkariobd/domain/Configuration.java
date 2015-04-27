package org.motechproject.nms.kilkariobd.domain;

import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.event.CrudEventType;

import javax.jdo.annotations.Unique;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This entity represents the configuration parameters of Kilkariobd.
 */
@Entity(recordHistory = true)
@CrudEvents(CrudEventType.UPDATE)
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
    private Integer freshObdPriority;

    @Field(required = true)
    private Integer retryDay1ObdPriority;

    @Field(required = true)
    private Integer retryDay2ObdPriority;

    @Field(required = true)
    private Integer retryDay3ObdPriority;

    @Field(required = true)
    private String obdFileServerIp;

    @Field(required = true)
    private String obdFilePathOnServer;

    @Field(required = true)
    private String obdFileServerSshUsername;

    @Field(required = true)
    private String obdIvrUrl;

    @Field(required = true)
    private String obdCreationEventCronExpression;

    @Field(required = true)
    private String obdNotificationEventCronExpression;


    @Field(required = true)
    private String purgeRecordsEventCronExpression;

    @Field(required = true)
    private Integer retryIntervalForObdPreparationInMins;


    @Field(required = true)
    private Integer maxObdPreparationRetryCount;



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

    public Integer getFreshObdPriority() {
        return freshObdPriority;
    }

    public void setFreshObdPriority(Integer freshObdPriority) {
        this.freshObdPriority = freshObdPriority;
    }

    public Integer getRetryDay1ObdPriority() {
        return retryDay1ObdPriority;
    }

    public void setRetryDay1ObdPriority(Integer retryDay1ObdPriority) {
        this.retryDay1ObdPriority = retryDay1ObdPriority;
    }

    public Integer getRetryDay2ObdPriority() {
        return retryDay2ObdPriority;
    }

    public void setRetryDay2ObdPriority(Integer retryDay2ObdPriority) {
        this.retryDay2ObdPriority = retryDay2ObdPriority;
    }

    public Integer getRetryDay3ObdPriority() {
        return retryDay3ObdPriority;
    }

    public void setRetryDay3ObdPriority(Integer retryDay3ObdPriority) {
        this.retryDay3ObdPriority = retryDay3ObdPriority;
    }

    public String getObdFileServerIp() {
        return obdFileServerIp;
    }

    public void setObdFileServerIp(String obdFileServerIp) {
        this.obdFileServerIp = obdFileServerIp;
    }

    public String getObdFilePathOnServer() {
        return obdFilePathOnServer;
    }

    public void setObdFilePathOnServer(String obdFilePathOnServer) {
        this.obdFilePathOnServer = obdFilePathOnServer;
    }

    public String getObdFileServerSshUsername() {
        return obdFileServerSshUsername;
    }

    public void setObdFileServerSshUsername(String obdFileServerSshUsername) {
        this.obdFileServerSshUsername = obdFileServerSshUsername;
    }

    public String getObdIvrUrl() {
        return obdIvrUrl;
    }

    public void setObdIvrUrl(String obdIvrUrl) {
        this.obdIvrUrl = obdIvrUrl;
    }

    public String getObdCreationEventCronExpression() {
        return obdCreationEventCronExpression;
    }

    public void setObdCreationEventCronExpression(String obdCreationEventCronExpression) {
        this.obdCreationEventCronExpression = obdCreationEventCronExpression;
    }

    public String getObdNotificationEventCronExpression() {
        return obdNotificationEventCronExpression;
    }

    public void setObdNotificationEventCronExpression(String obdNotificationEventCronExpression) {
        this.obdNotificationEventCronExpression = obdNotificationEventCronExpression;
    }

    public Integer getRetryIntervalForObdPreparationInMins() {
        return retryIntervalForObdPreparationInMins;
    }

    public void setRetryIntervalForObdPreparationInMins(Integer retryIntervalForObdPreparationInMins) {
        this.retryIntervalForObdPreparationInMins = retryIntervalForObdPreparationInMins;
    }

    public Integer getMaxObdPreparationRetryCount() {
        return maxObdPreparationRetryCount;
    }

    public void setMaxObdPreparationRetryCount(Integer maxObdPreparationRetryCount) {
        this.maxObdPreparationRetryCount = maxObdPreparationRetryCount;
    }

    public String getPurgeRecordsEventCronExpression() {
        return purgeRecordsEventCronExpression;
    }

    public void setPurgeRecordsEventCronExpression(String purgeRecordsEventCronExpression) {
        this.purgeRecordsEventCronExpression = purgeRecordsEventCronExpression;
    }


}

