package org.motechproject.nms.kilkari.ut.domain;


import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.*;
import org.motechproject.nms.masterdata.domain.*;

import java.util.Set;

public class SubscriberTest {

    BeneficiaryType beneficiaryType = BeneficiaryType.MOTHER;
    DeactivationReason deactivationReason = DeactivationReason.INVALID_MSISDN;
    District district = getDistrictData();
    HealthBlock healthBlock = getHealthBlockData();
    HealthFacility phc = getHealthFacilityData();
    HealthSubFacility healthSubFacility = getHealthSubFacilityData();
    Taluka taluka = getTalukaData();
    Village village = getVillageData();
    DateTime dateTime = new DateTime();
    Set<Subscription> subscriptionList = null;
    State state = null;

    Subscriber subscriber = new Subscriber();

    @Test
    public void shouldSetValuesInSubscriber() {

        subscriber = createSubscriber();

        Assert.assertEquals("name", subscriber.getName());
        Assert.assertEquals("aadharNumber", subscriber.getAadharNumber());
        Assert.assertTrue(26 == subscriber.getAge());
        Assert.assertEquals(beneficiaryType, subscriber.getBeneficiaryType());
        Assert.assertEquals("childMctsId", subscriber.getChildMctsId());
        Assert.assertEquals(deactivationReason, subscriber.getDeactivationReason());
        Assert.assertEquals(district, subscriber.getDistrict());
        Assert.assertEquals(dateTime, subscriber.getDob());
        Assert.assertEquals(healthBlock, subscriber.getHealthBlock());
        Assert.assertTrue(12 == subscriber.getLanguageLocationCode());
        Assert.assertEquals(dateTime.minus(3L), subscriber.getLmp());
        Assert.assertEquals("motherMctsId", subscriber.getMotherMctsId());
        Assert.assertEquals("msisdn",subscriber.getMsisdn());
        Assert.assertEquals(phc, subscriber.getPhc());
        Assert.assertEquals(state, subscriber.getState());
        Assert.assertEquals(healthSubFacility, subscriber.getSubCentre());
        Assert.assertEquals(subscriptionList, subscriber.getSubscriptionList());
        Assert.assertEquals(taluka, subscriber.getTaluka());
        Assert.assertEquals(village, subscriber.getVillage());
    }

    @Test
    public void shouldGetPackNamePACK_72_WEEKS() {

        SubscriptionPack subscriptionPack = SubscriptionPack.PACK_72_WEEKS;

        subscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
        Assert.assertEquals(subscriptionPack, subscriber.getSuitablePackName());
    }

    @Test
    public void shouldGetPackNamePACK_48_WEEKS() {

        SubscriptionPack subscriptionPack = SubscriptionPack.PACK_48_WEEKS;

        subscriber.setBeneficiaryType(BeneficiaryType.CHILD);
        Assert.assertEquals(subscriptionPack, subscriber.getSuitablePackName());
    }

    @Test
    public void shouldGetMotherMctsId() {

        subscriber.setBeneficiaryType(BeneficiaryType.MOTHER);
        subscriber.setChildMctsId("childMctsId");
        subscriber.setMotherMctsId("motherMctsId");

        Assert.assertEquals("motherMctsId", subscriber.getSuitableMctsId());
    }

    @Test
    public void shouldGetchildMctsId() {

        subscriber.setBeneficiaryType(BeneficiaryType.CHILD);
        subscriber.setChildMctsId("childMctsId");
        subscriber.setMotherMctsId("motherMctsId");

        Assert.assertEquals("childMctsId", subscriber.getSuitableMctsId());
    }

    @Test
    public void shouldGetDob() {


        subscriber.setBeneficiaryType(BeneficiaryType.CHILD);
        subscriber.setDob(dateTime);
        subscriber.setLmp(dateTime.minus(3L));

        Assert.assertEquals(dateTime, subscriber.getDob());
    }

    @Test
    public void shouldGetLmp() {


        subscriber.setBeneficiaryType(BeneficiaryType.CHILD);
        subscriber.setDob(dateTime);
        subscriber.setLmp(dateTime.minus(3L));

        Assert.assertEquals(dateTime.minus(3L), subscriber.getLmp());
    }


    public Subscriber createSubscriber() {

        Subscriber subscriber = new Subscriber();

        subscriber.setName("name");
        subscriber.setAadharNumber("aadharNumber");
        subscriber.setAge(26);
        subscriber.setBeneficiaryType(beneficiaryType);
        subscriber.setChildMctsId("childMctsId");
        subscriber.setDeactivationReason(deactivationReason);
        subscriber.setDistrict(district);
        subscriber.setDob(dateTime);
        subscriber.setHealthBlock(healthBlock);
        subscriber.setLanguageLocationCode(12);
        subscriber.setLmp(dateTime.minus(3L));
        subscriber.setMotherMctsId("motherMctsId");
        subscriber.setMsisdn("msisdn");
        subscriber.setPhc(phc);
        subscriber.setState(state);
        subscriber.setSubCentre(healthSubFacility);
        subscriber.setSubscriptionList(subscriptionList);
        subscriber.setTaluka(taluka);
        subscriber.setVillage(village);

        return subscriber;
    }

    private District getDistrictData() {

        District district = new District();
        district.setStateCode(1L);
        district.setDistrictCode(456L);
        district.setName("districtName");

        return district;
    }

    private HealthBlock getHealthBlockData() {

        HealthBlock healthBlock = new HealthBlock();
        healthBlock.setStateCode(1L);
        healthBlock.setDistrictCode(456L);
        healthBlock.setTalukaCode(8L);
        healthBlock.setHealthBlockCode(1002L);
        healthBlock.setName("healthBlockName");

        return healthBlock;
    }

    private HealthFacility getHealthFacilityData() {

        HealthFacility healthFacility = new HealthFacility();
        healthFacility.setStateCode(1L);
        healthFacility.setDistrictCode(456L);
        healthFacility.setTalukaCode(8L);
        healthFacility.setHealthBlockCode(1002L);
        healthFacility.setHealthFacilityCode(1111L);

        return healthFacility;
    }

    private HealthSubFacility getHealthSubFacilityData() {

        HealthSubFacility healthSubFacilityData = new HealthSubFacility();

        healthSubFacilityData.setStateCode(1L);
        healthSubFacilityData.setDistrictCode(456L);
        healthSubFacilityData.setTalukaCode(8L);
        healthSubFacilityData.setHealthBlockCode(1002L);
        healthSubFacilityData.setHealthFacilityCode(1111L);
        healthSubFacilityData.setHealthSubFacilityCode(9999L);

        return healthSubFacilityData;
    }

    private Taluka getTalukaData() {

        Taluka taluka = new Taluka();
        taluka.setStateCode(1L);
        taluka.setDistrictCode(456L);
        taluka.setTalukaCode(8L);

        return taluka;
    }

    private Village getVillageData() {

        Village villageData = new Village();
        villageData.setStateCode(123L);
        villageData.setDistrictCode(456L);
        villageData.setTalukaCode(8L);
        villageData.setVillageCode(1002L);
        return villageData;
    }
}