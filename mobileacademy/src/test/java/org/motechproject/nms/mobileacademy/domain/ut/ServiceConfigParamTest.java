package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.mobileacademy.domain.ServiceConfigParam;

public class ServiceConfigParamTest {

    ServiceConfigParam serviceConfigParam = new ServiceConfigParam();

    @Test
    public void testGetIndex() {
        serviceConfigParam.setIndex(new Long(123));
        assertEquals(new Long(123), serviceConfigParam.getIndex());
    }

    @Test
    public void testGetCappingType() {
        serviceConfigParam.setCappingType(1);
        assertSame(1, serviceConfigParam.getCappingType());
    }

    @Test
    public void testGetNationalCapValue() {
        serviceConfigParam.setNationalCapValue(30);
        assertSame(30, serviceConfigParam.getNationalCapValue());
    }

    @Test
    public void testGetMaxEndOfUsuageMessage() {
        serviceConfigParam.setMaxEndOfUsuageMessage(2);

        assertSame(2, serviceConfigParam.getMaxEndOfUsuageMessage());
    }

    @Test
    public void testGetCourseQualifyingScore() {
        serviceConfigParam.setCourseQualifyingScore(22);

        assertSame(22, serviceConfigParam.getCourseQualifyingScore());
    }

    @Test
    public void testGetDefaultLanguageLocationCode() {
        serviceConfigParam.setDefaultLanguageLocationCode(11);

        assertSame(11, serviceConfigParam.getDefaultLanguageLocationCode());
    }

    @Test
    public void testGetSmsSenderAddress() {
        serviceConfigParam.setSmsSenderAddress("9999999999");

        assertSame("9999999999", serviceConfigParam.getSmsSenderAddress());
    }

    @Test
    public void testServiceConfigParamInstance() {
        ServiceConfigParam serviceConfigParam = new ServiceConfigParam(1l, 2,
                1, 1, 0, 0, "Addr");
        Assert.assertNotNull(serviceConfigParam);
    }
}
