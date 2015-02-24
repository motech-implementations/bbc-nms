package org.motechproject.nms.kilkari.domain;

import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Persistent;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;

/**
 * This entity represents the subscriber record.
 */
@Entity(recordHistory=true)
public class Subscriber extends MdsEntity {
    
    @Field(required = true)
    private String msisdn;
    
    @Field
    private String oldMsisdn;
    
    @Field
    private String childMctsId;
    
    @Field
    private String motherMctsId;
    
    @Field(required = true)
    private BeneficiaryType beneficiaryType;
    
    @Field
    private String name;
    
    @Field
    private Integer age;
    
    @Field(name = "state_id")
    private State state;
    
    @Field(name = "district_id")
    private District districtId;
    
    @Field(name = "taluka_id")
    private Taluka talukaId;
    
    @Field(name = "healthBlock_id")
    private HealthBlock healthBlockId;
    
    @Field(name = "phc_id")
    private HealthFacility phcId;
    
    @Field(name = "subCentre_id")
    private HealthSubFacility subCentreId;
    
    @Field(name = "village_id")
    private Village villageId;
    
    @Field
    @Column(length = 2)
    private Integer languageLocationCode;
    
    @Field
    private String aadharNumber;
    
    @Field
    private DateTime lmp;
    
    @Field
    private DateTime dob;
    
    @Field
    private Boolean stillBirth;
    
    @Field
    private Boolean abortion;
    
    @Field
    private Boolean motherDeath;
    
    @Field
    private Boolean childDeath;
    
    @Persistent(mappedBy = "subscriber")
    private List<Subscription> subscriptionList;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getOldMsisdn() {
        return oldMsisdn;
    }

    public void setOldMsisdn(String oldMsisdn) {
        this.oldMsisdn = oldMsisdn;
    }

    public String getChildMctsId() {
        return childMctsId;
    }

    public void setChildMctsId(String childMctsId) {
        this.childMctsId = childMctsId;
    }

    public String getMotherMctsId() {
        return motherMctsId;
    }

    public void setMotherMctsId(String motherMctsId) {
        this.motherMctsId = motherMctsId;
    }

    public BeneficiaryType getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(BeneficiaryType beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public District getDistrictId() {
        return districtId;
    }

    public void setDistrictId(District districtId) {
        this.districtId = districtId;
    }

    public Taluka getTalukaId() {
        return talukaId;
    }

    public void setTalukaId(Taluka talukaId) {
        this.talukaId = talukaId;
    }

    public HealthBlock getHealthBlockId() {
        return healthBlockId;
    }

    public void setHealthBlockId(HealthBlock healthBlockId) {
        this.healthBlockId = healthBlockId;
    }

    public HealthFacility getPhcId() {
        return phcId;
    }

    public void setPhcId(HealthFacility phcId) {
        this.phcId = phcId;
    }

    public HealthSubFacility getSubCentreId() {
        return subCentreId;
    }

    public void setSubCentreId(HealthSubFacility subCentreId) {
        this.subCentreId = subCentreId;
    }

    public Village getVillageId() {
        return villageId;
    }

    public void setVillageId(Village villageId) {
        this.villageId = villageId;
    }

    public Integer getLanguageLocationCode() {
        return languageLocationCode;
    }

    public void setLanguageLocationCode(Integer languageLocationCode) {
        this.languageLocationCode = languageLocationCode;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public DateTime getLmp() {
        return lmp;
    }

    public void setLmp(DateTime lmp) {
        this.lmp = lmp;
    }

    public DateTime getDob() {
        return dob;
    }

    public void setDob(DateTime dob) {
        this.dob = dob;
    }

    public Boolean getStillBirth() {
        return stillBirth;
    }

    public void setStillBirth(Boolean stillBirth) {
        this.stillBirth = stillBirth;
    }

    public Boolean getAbortion() {
        return abortion;
    }

    public void setAbortion(Boolean abortion) {
        this.abortion = abortion;
    }

    public Boolean getMotherDeath() {
        return motherDeath;
    }

    public void setMotherDeath(Boolean motherDeath) {
        this.motherDeath = motherDeath;
    }

    public Boolean getChildDeath() {
        return childDeath;
    }

    public void setChildDeath(Boolean childDeath) {
        this.childDeath = childDeath;
    }

    public List<Subscription> getSubscriptionList() {
        return subscriptionList;
    }

    public void setSubscriptionList(List<Subscription> subscriptionList) {
        this.subscriptionList = subscriptionList;
    }

}
