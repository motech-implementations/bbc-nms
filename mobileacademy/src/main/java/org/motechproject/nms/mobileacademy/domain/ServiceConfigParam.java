package org.motechproject.nms.mobileacademy.domain;


import javax.jdo.annotations.Unique;
import javax.validation.constraints.Size;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * ServiceConfigParam object to refer Mobile Academy Service configuration
 * Parameters.
 *
 */
@Entity
public class ServiceConfigParam extends MdsEntity {

    @Field(required = true)
    @Size(min = 1, max = 1)
    @Unique
    private Long index;

    private Integer nmsMaCappingType;

    private Integer nmsMaNationalCapValue;

    private Integer nmsMaMaxEndOfUsuageMessage;

    private Integer nmsMaCourseQualifyingScore;

    private Integer nmsMaDefaultLanguageLocationCode;

    private String nmsMaSmsSenderAddress;

    /**
     * constructor with 0 arguments.
     */
    public ServiceConfigParam() {

    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Integer getNmsMaCappingType() {
        return nmsMaCappingType;
    }

    public void setNmsMaCappingType(Integer nmsMaCappingType) {
        this.nmsMaCappingType = nmsMaCappingType;
    }

    public Integer getNmsMaNationalCapValue() {
        return nmsMaNationalCapValue;
    }

    public void setNmsMaNationalCapValue(Integer nmsMaNationalCapValue) {
        this.nmsMaNationalCapValue = nmsMaNationalCapValue;
    }

    public Integer getNmsMaMaxEndOfUsuageMessage() {
        return nmsMaMaxEndOfUsuageMessage;
    }

    public void setNmsMaMaxEndOfUsuageMessage(Integer nmsMaMaxEndOfUsuageMessage) {
        this.nmsMaMaxEndOfUsuageMessage = nmsMaMaxEndOfUsuageMessage;
    }

    public Integer getNmsMaCourseQualifyingScore() {
        return nmsMaCourseQualifyingScore;
    }

    public void setNmsMaCourseQualifyingScore(Integer nmsMaCourseQualifyingScore) {
        this.nmsMaCourseQualifyingScore = nmsMaCourseQualifyingScore;
    }

    public Integer getNmsMaDefaultLanguageLocationCode() {
        return nmsMaDefaultLanguageLocationCode;
    }

    public void setNmsMaDefaultLanguageLocationCode(
            Integer nmsMaDefaultLanguageLocationCode) {
        this.nmsMaDefaultLanguageLocationCode = nmsMaDefaultLanguageLocationCode;
    }

    public String getNmsMaSmsSenderAddress() {
        return nmsMaSmsSenderAddress;
    }

    public void setNmsMaSmsSenderAddress(String nmsMaSmsSenderAddress) {
        this.nmsMaSmsSenderAddress = nmsMaSmsSenderAddress;
    }

}
