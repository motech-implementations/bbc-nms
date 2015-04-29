package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.mobileacademy.domain.Configuration;

public class ConfigurationTest {

    Configuration configuration = new Configuration();

    /*
     * Test the Index of the Configuration
     */
    @Test
    public void testGetIndex() {
        configuration.setIndex(new Long(123));
        assertEquals(new Long(123), configuration.getIndex());
    }

    /*
     * Test the Capping Type of the Configuration
     */
    @Test
    public void testGetCappingType() {
        configuration.setCappingType(1);
        assertSame(1, configuration.getCappingType());
    }

    /*
     * Test the National Cap value of the Configuration
     */
    @Test
    public void testGetNationalCapValue() {
        configuration.setNationalCapValue(30);
        assertSame(30, configuration.getNationalCapValue());
    }

    /*
     * Test the Max Allowed End Of Usage Prompt Configuration
     */
    @Test
    public void testGetMaxAllowedEndOfUsagePrompt() {
        configuration.setMaxAllowedEndOfUsagePrompt(2);

        assertSame(2, configuration.getMaxAllowedEndOfUsagePrompt());
    }

    /*
     * Test the Course Qualifying Score of the Configuration
     */
    @Test
    public void testGetCourseQualifyingScore() {
        configuration.setCourseQualifyingScore(22);

        assertSame(22, configuration.getCourseQualifyingScore());
    }

    /*
     * Test the Default LanguageLocationCode of the Configuration
     */
    @Test
    public void testGetDefaultLanguageLocationCode() {
        configuration.setDefaultLanguageLocationCode("11");

        assertSame("11", configuration.getDefaultLanguageLocationCode());
    }

    /*
     * Test the sms sender address of the Configuration
     */
    @Test
    public void testGetSmsSenderAddress() {
        configuration.setSmsSenderAddress("9999999999");

        assertSame("9999999999", configuration.getSmsSenderAddress());
    }

    /*
     * Test the Instance of the Configuration
     */
    @Test
    public void testServiceConfigParamInstance() {
        Configuration configuration = new Configuration(1l, 2, 1, 1, 0, "0",
                "Addr");
        Assert.assertNotNull(configuration);
    }
}
