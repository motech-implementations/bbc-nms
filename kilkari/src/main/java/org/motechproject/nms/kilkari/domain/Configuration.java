package org.motechproject.nms.kilkari.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import javax.jdo.annotations.Unique;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This entity represents the number of messages per week in a pack.
 */
@Entity(recordHistory = true)
public class Configuration {

    @Unique
    @Field(required = true)
    @Min(1)
    @Max(1)
    @NotNull
    private Long index;
    
    @Field(required = true)
    private Integer nmsKk72WeeksPackMsgsPerWeek;
    
    @Field(required = true)
    private Integer nmsKk48WeeksPackMsgsPerWeek;
    
    @Field(required = true)
    private Integer nmsKkMaxAllowedActiveBeneficiaryCount;
    
    @Field(required = true)
    private String nmsKkFreshObdServiceId;
    
    @Field(required = true)
    private String nmsKkRetryDay1ObdServiceId;
    
    @Field(required = true)
    private String nmsKkRetryDay2ObdServiceId;
    
    @Field(required = true)
    private String nmsKkRetryDay3ObdServiceId;
    
    @Field(required = true)
    private String nmsKkFreshObdPriority;
    
    @Field(required = true)
    private String nmsKkRetryDay1ObdPriority;
    
    @Field(required = true)
    private String nmsKkRetryDay2ObdPriority;
    
    @Field(required = true)
    private String nmsKkRetryDay3ObdPriority;

    public Long getIndex() {
        return index;
    }

    /* Hardcoded to ConfigurationConstants.CONFIGURATION_INDEX */
    public void setIndex(Long index) {
        this.index = 1L;
    }

    public Integer getNmsKk72WeeksPackMsgsPerWeek() {
        return nmsKk72WeeksPackMsgsPerWeek;
    }

    public void setNmsKk72WeeksPackMsgsPerWeek(Integer nmsKk72WeeksPackMsgsPerWeek) {
        this.nmsKk72WeeksPackMsgsPerWeek = nmsKk72WeeksPackMsgsPerWeek;
    }

    public Integer getNmsKk48WeeksPackMsgsPerWeek() {
        return nmsKk48WeeksPackMsgsPerWeek;
    }

    public void setNmsKk48WeeksPackMsgsPerWeek(Integer nmsKk48WeeksPackMsgsPerWeek) {
        this.nmsKk48WeeksPackMsgsPerWeek = nmsKk48WeeksPackMsgsPerWeek;
    }

    public Integer getNmsKkMaxAllowedActiveBeneficiaryCount() {
        return nmsKkMaxAllowedActiveBeneficiaryCount;
    }

    public void setNmsKkMaxAllowedActiveBeneficiaryCount(Integer nmsKkMaxAllowedActiveBeneficiaryCount) {
        this.nmsKkMaxAllowedActiveBeneficiaryCount = nmsKkMaxAllowedActiveBeneficiaryCount;
    }

    public String getNmsKkFreshObdServiceId() {
        return nmsKkFreshObdServiceId;
    }

    public void setNmsKkFreshObdServiceId(String nmsKkFreshObdServiceId) {
        this.nmsKkFreshObdServiceId = nmsKkFreshObdServiceId;
    }

    public String getNmsKkRetryDay1ObdServiceId() {
        return nmsKkRetryDay1ObdServiceId;
    }

    public void setNmsKkRetryDay1ObdServiceId(String nmsKkRetryDay1ObdServiceId) {
        this.nmsKkRetryDay1ObdServiceId = nmsKkRetryDay1ObdServiceId;
    }

    public String getNmsKkRetryDay2ObdServiceId() {
        return nmsKkRetryDay2ObdServiceId;
    }

    public void setNmsKkRetryDay2ObdServiceId(String nmsKkRetryDay2ObdServiceId) {
        this.nmsKkRetryDay2ObdServiceId = nmsKkRetryDay2ObdServiceId;
    }

    public String getNmsKkRetryDay3ObdServiceId() {
        return nmsKkRetryDay3ObdServiceId;
    }

    public void setNmsKkRetryDay3ObdServiceId(String nmsKkRetryDay3ObdServiceId) {
        this.nmsKkRetryDay3ObdServiceId = nmsKkRetryDay3ObdServiceId;
    }

    public String getNmsKkFreshObdPriority() {
        return nmsKkFreshObdPriority;
    }

    public void setNmsKkFreshObdPriority(String nmsKkFreshObdPriority) {
        this.nmsKkFreshObdPriority = nmsKkFreshObdPriority;
    }

    public String getNmsKkRetryDay1ObdPriority() {
        return nmsKkRetryDay1ObdPriority;
    }

    public void setNmsKkRetryDay1ObdPriority(String nmsKkRetryDay1ObdPriority) {
        this.nmsKkRetryDay1ObdPriority = nmsKkRetryDay1ObdPriority;
    }

    public String getNmsKkRetryDay2ObdPriority() {
        return nmsKkRetryDay2ObdPriority;
    }

    public void setNmsKkRetryDay2ObdPriority(String nmsKkRetryDay2ObdPriority) {
        this.nmsKkRetryDay2ObdPriority = nmsKkRetryDay2ObdPriority;
    }

    public String getNmsKkRetryDay3ObdPriority() {
        return nmsKkRetryDay3ObdPriority;
    }

    public void setNmsKkRetryDay3ObdPriority(String nmsKkRetryDay3ObdPriority) {
        this.nmsKkRetryDay3ObdPriority = nmsKkRetryDay3ObdPriority;
    }
}
