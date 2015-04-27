package org.motechproject.nms.kilkari.ut.domain;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.domain.Configuration;

public class ConfigurationTest {

    @Test
    public void shouldSetValuesInConfiguration() {

        Configuration configuration = new Configuration();

        configuration.setIndex(1L);
        Assert.assertTrue(1L == configuration.getIndex());

        configuration.setMaxAllowedActiveBeneficiaryCount(20);
        Assert.assertTrue(20 == configuration.getMaxAllowedActiveBeneficiaryCount());

        configuration.setNationalDefaultLanguageLocationCode("123");
        Assert.assertTrue("123".equals(configuration.getNationalDefaultLanguageLocationCode()));

        configuration.setNumMsgPerWeek(10);
        Assert.assertTrue(10 == configuration.getNumMsgPerWeek());
    }

}
