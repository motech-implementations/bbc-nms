package org.motechproject.nms.frontlineworker.ut.domain;

import org.junit.Test;
import org.motechproject.nms.frontlineworker.domain.CsvFrontLineWorker;

import static org.junit.Assert.assertEquals;

/**
 * This class performs the UT of CsvFrontLineWorker.
 */
public class CsvFrontLineWorkerTest {

    CsvFrontLineWorker csvFrontLineWorker = new CsvFrontLineWorker();


    @Test
    public void testShouldSetWithFlwId() {
        csvFrontLineWorker.setFlwId("1");
        assertEquals("Flw Id[1]", csvFrontLineWorker.toString());
    }

    @Test
    public void testShouldSetWithContactNumber() {
        csvFrontLineWorker.setFlwId("");
        csvFrontLineWorker.setContactNo("1234");
        assertEquals("Contact No[1234]", csvFrontLineWorker.toString());
    }

    @Test
    public void testShouldSetWithAlternateContactNumber() {
        csvFrontLineWorker.setAlternateContactNo("12345");
        assertEquals("12345", csvFrontLineWorker.getAlternateContactNo());
    }

    @Test
    public void testShouldSetWithOldContactNumber() {
        csvFrontLineWorker.setOldMobileNo("123456");
        assertEquals("123456", csvFrontLineWorker.getOldMobileNo());
    }

    @Test
    public void testShouldSetWithFlwIdNull() {
        csvFrontLineWorker.setContactNo("1234");
        assertEquals("Contact No[1234]", csvFrontLineWorker.toString());
    }

    @Test
    public void testShouldSetWithName() {
        csvFrontLineWorker.setName("Etasha");
        assertEquals("Etasha", csvFrontLineWorker.getName());
    }

    @Test
    public void testShouldSetWithType() {
        csvFrontLineWorker.setType("USHA");
        assertEquals("USHA", csvFrontLineWorker.getType());
    }

    @Test
    public void testShouldSetWithTalukaCode() {
        csvFrontLineWorker.setTalukaCode("1");
        assertEquals("1", csvFrontLineWorker.getTalukaCode());
    }

    @Test
    public void testShouldSetWithPhcCode() {
        csvFrontLineWorker.setPhcCode("1234");
        assertEquals("1234", csvFrontLineWorker.getPhcCode());
    }

    @Test
    public void testShouldSetWithSubCentreCode() {
        csvFrontLineWorker.setSubCentreCode("12345");
        assertEquals("12345", csvFrontLineWorker.getSubCentreCode());
    }

    @Test
    public void testShouldSetWithVillageCode() {
        csvFrontLineWorker.setVillageCode("1234");
        assertEquals("1234", csvFrontLineWorker.getVillageCode());
    }

    @Test
    public void testShouldSetWithAdhaarNo() {
        csvFrontLineWorker.setAdhaarNo("1234");
        assertEquals("1234", csvFrontLineWorker.getAdhaarNo());
    }

    @Test
    public void testShouldSetWithIsValid() {
        csvFrontLineWorker.setIsValid("True");
        assertEquals("True", csvFrontLineWorker.getIsValid());
    }

    @Test
    public void testShouldSetWithNmsFlwId() {
        csvFrontLineWorker.setNmsFlwId("null");
        assertEquals("null", csvFrontLineWorker.getNmsFlwId());
    }

    @Test
    public void testShouldSetWithHealthBlockCode() {
        csvFrontLineWorker.setHealthBlockCode("1234");
        assertEquals("1234", csvFrontLineWorker.getHealthBlockCode());
    }

    @Test
    public void testShouldSetWithDistrictCode() {
        csvFrontLineWorker.setDistrictCode("1234");
        assertEquals("1234", csvFrontLineWorker.getDistrictCode());
    }

    @Test
    public void testShouldSetWithStateCode() {
        csvFrontLineWorker.setStateCode("12");
        assertEquals("12", csvFrontLineWorker.getStateCode());
    }


}

