package org.motechproject.nms.mobileacademy.domain;

import javax.jdo.annotations.Unique;
import javax.validation.constraints.Size;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * Configuration object to refer Mobile Academy Service configuration
 * Parameters.
 *
 */
@Entity
public class Configuration extends MdsEntity {

    @Field(required = true)
    @Size(min = 1, max = 1)
    @Unique
    private Long index;

    @Field
    private Integer cappingType;

    @Field
    private Integer nationalCapValue;

    @Field
    private Integer maxAllowedEndOfUsagePrompt;

    @Field
    private Integer courseQualifyingScore;

    @Field
    private Integer defaultLanguageLocationCode;

    @Field
    private String smsSenderAddress;

    /**
     * constructor with 0 arguments.
     */
    public Configuration() {

    }

    /**
     * constructor with all arguments.
     * 
     * @param index unique record identifier
     * @param CappingType specify capping type: 0-No capping 1-National Capping
     *            2-State wise capping
     * @param NationalCapValue specify national cap value.
     * @param maxAllowedEndOfUsagePrompt Maximum no. of times end of usage
     *            message can be played to the user on usage unavailability.
     *            After the expiry of this value, call will be simply dropped.
     * @param CourseQualifyingScore Minimum score a user should achieve in order
     *            to qualify the MA course.
     * @param DefaultLanguageLocationCode language Location Code value in case
     *            circle for a user could not be determined.
     * @param SmsSenderAddress Address to be populated in from field of SMS
     *            which will be sent on successful completion of course.
     */
    public Configuration(Long index, Integer cappingType,
            Integer nationalCapValue, Integer maxAllowedEndOfUsagePrompt,
            Integer courseQualifyingScore, Integer defaultLanguageLocationCode,
            String smsSenderAddress) {
        this.index = index;
        this.cappingType = cappingType;
        this.nationalCapValue = nationalCapValue;
        this.maxAllowedEndOfUsagePrompt = maxAllowedEndOfUsagePrompt;
        this.courseQualifyingScore = courseQualifyingScore;
        this.defaultLanguageLocationCode = defaultLanguageLocationCode;
        this.smsSenderAddress = smsSenderAddress;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Integer getCappingType() {
        return cappingType;
    }

    public void setCappingType(Integer cappingType) {
        this.cappingType = cappingType;
    }

    public Integer getNationalCapValue() {
        return nationalCapValue;
    }

    public void setNationalCapValue(Integer nationalCapValue) {
        this.nationalCapValue = nationalCapValue;
    }

    public Integer getMaxAllowedEndOfUsagePrompt() {
        return maxAllowedEndOfUsagePrompt;
    }

    public void setMaxAllowedEndOfUsagePrompt(Integer maxAllowedEndOfUsagePrompt) {
        this.maxAllowedEndOfUsagePrompt = maxAllowedEndOfUsagePrompt;
    }

    public Integer getCourseQualifyingScore() {
        return courseQualifyingScore;
    }

    public void setCourseQualifyingScore(Integer courseQualifyingScore) {
        this.courseQualifyingScore = courseQualifyingScore;
    }

    public Integer getDefaultLanguageLocationCode() {
        return defaultLanguageLocationCode;
    }

    public void setDefaultLanguageLocationCode(
            Integer defaultLanguageLocationCode) {
        this.defaultLanguageLocationCode = defaultLanguageLocationCode;
    }

    public String getSmsSenderAddress() {
        return smsSenderAddress;
    }

    public void setSmsSenderAddress(String smsSenderAddress) {
        this.smsSenderAddress = smsSenderAddress;
    }

}