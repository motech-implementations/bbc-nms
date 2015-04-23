package org.motechproject.nms.frontlineworker.ut.domain;

import org.junit.Test;
import org.motechproject.nms.frontlineworker.domain.UserProfile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class performs the UT of UserProfile.
 */
public class UserProfileTest {

    UserProfile userProfile = new UserProfile();

    @Test
    public void testNmsId() {

        userProfile.setNmsFlwId(12L);
        assertTrue(12L == userProfile.getNmsFlwId());
    }

    @Test
    public void testMsisdn() {

        userProfile.setMsisdn("1234567890");
        assertEquals("1234567890", userProfile.getMsisdn());
    }

    @Test
    public void testIsDefaultLanguageLocationCode() {

        userProfile.setIsDefaultLanguageLocationCode(true);
        assertEquals(true, userProfile.isDefaultLanguageLocationCode());
    }

    @Test
    public void testLanguageLocationCode() {

        userProfile.setLanguageLocationCode("LLC");
        assertEquals("LLC", userProfile.getLanguageLocationCode());
    }

    @Test
    public void testCircle() {

        userProfile.setCircle("12");
        assertEquals("12", userProfile.getCircle());
    }

    @Test
    public void testMaxStateLevelCappingValue() {

        userProfile.setMaxStateLevelCappingValue(10);
        assertTrue(10 == userProfile.getMaxStateLevelCappingValue());
    }

    @Test
    public void testToString() {

        userProfile.setNmsFlwId(12L);
        userProfile.setMsisdn("1234567890");
        userProfile.setIsDefaultLanguageLocationCode(true);
        userProfile.setLanguageLocationCode("LLC");
        userProfile.setCircle("12");
        userProfile.setMaxStateLevelCappingValue(10);

        assertEquals("UserProfile{" +
                        ", nmsFlwId=" + userProfile.getNmsFlwId() +
                        ", msisdn='" + userProfile.getMsisdn() + '\'' +
                        ", isDefaultLanguageLocationCode=" + userProfile.isDefaultLanguageLocationCode() +
                        ", languageLocationCode=" + userProfile.getLanguageLocationCode() +
                        ", circle='" + userProfile.getCircle() + '\'' +
                        ", maxStateLevelCappingValue=" + userProfile.getMaxStateLevelCappingValue() + "}",
                userProfile.toString()
        );
        //assertNotNull(userProfile.toString());

    }


}
