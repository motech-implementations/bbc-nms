package org.motechproject.nms.mobileacademy.domain.ut;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.mobileacademy.domain.ServiceConfigParam;

public class ServiceConfigParamTest {

    ServiceConfigParam serviceConfigParam = new ServiceConfigParam();

    /*
	 * Test the Index of the Service Config Param
	 */
    @Test
    public void testGetIndex() {
        serviceConfigParam.setIndex(new Long(123));
        assertEquals(new Long(123), serviceConfigParam.getIndex());
    }

    /*
   	 * Test the Capping Type of the Service Config Param
   	 */
    @Test
    public void testGetCappingType() {
        serviceConfigParam.setCappingType(1);
        assertSame(1, serviceConfigParam.getCappingType());
    }

    /*
   	 * Test the National Cap value of the Service Config Param
   	 */
    @Test
    public void testGetNationalCapValue() {
        serviceConfigParam.setNationalCapValue(30);
        assertSame(30, serviceConfigParam.getNationalCapValue());
    }

    /*
   	 * Test the Max End Of Usage Message of the Service Config Param
   	 */
    @Test
    public void testGetMaxEndOfUsuageMessage() {
        serviceConfigParam.setMaxEndOfUsuageMessage(2);

        assertSame(2, serviceConfigParam.getMaxEndOfUsuageMessage());
    }

    /*
   	 * Test the Course Qualifying Score of the Service Config Param
   	 */
    @Test
    public void testGetCourseQualifyingScore() {
        serviceConfigParam.setCourseQualifyingScore(22);

        assertSame(22, serviceConfigParam.getCourseQualifyingScore());
    }

    /*
   	 * Test the Default LanguageLocationCode of the Service Config Param
   	 */
    @Test
    public void testGetDefaultLanguageLocationCode() {
        serviceConfigParam.setDefaultLanguageLocationCode(11);

        assertSame(11, serviceConfigParam.getDefaultLanguageLocationCode());
    }

    /*
   	 * Test the sms sender address of the Service Config Param
   	 */
    @Test
    public void testGetSmsSenderAddress() {
        serviceConfigParam.setSmsSenderAddress("9999999999");

        assertSame("9999999999", serviceConfigParam.getSmsSenderAddress());
    }

    /*
   	 * Test the Instance of the Service Config Param
   	 */
    @Test
    public void testServiceConfigParamInstance() {
        ServiceConfigParam serviceConfigParam = new ServiceConfigParam(1l, 2,
                1, 1, 0, 0, "Addr");
        Assert.assertNotNull(serviceConfigParam);
    }
}
