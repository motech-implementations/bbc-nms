package org.motechproject.nms.frontlineworker.ut.domain;

import org.junit.Test;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;

import static org.junit.Assert.assertEquals;

/**
 * This class performs the UT of FrontLineWorkerCsv.
 */
public class FrontLineWorkerCsvTest {

    FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv();


    @Test
    public void testShouldSetWithFlwId() {
        frontLineWorkerCsv.setFlwId("1");
        assertEquals("Flw Id[1]", frontLineWorkerCsv.toString());
    }

    @Test
    public void testShouldSetWithContactNumber() {
        frontLineWorkerCsv.setFlwId("");
        frontLineWorkerCsv.setContactNo("1234");
        assertEquals("Contact No[1234]", frontLineWorkerCsv.toString());
    }

    @Test
    public void testShouldSetWithFlwIdNull() {
        frontLineWorkerCsv.setContactNo("1234");
        assertEquals("Contact No[1234]", frontLineWorkerCsv.toString());
    }

    @Test
    public void testShouldSetWithName() {
        frontLineWorkerCsv.setName("Etasha");
        assertEquals("Etasha", frontLineWorkerCsv.getName());
    }

    @Test
    public void testShouldSetWithType() {
        frontLineWorkerCsv.setType("USHA");
        assertEquals("USHA", frontLineWorkerCsv.getType());
    }

    @Test
    public void testShouldSetWithTalukaCode() {
        frontLineWorkerCsv.setTalukaCode("1");
        assertEquals("1", frontLineWorkerCsv.getTalukaCode());
    }

    @Test
    public void testShouldSetWithPhcCode() {
        frontLineWorkerCsv.setPhcCode("1234");
        assertEquals("1234", frontLineWorkerCsv.getPhcCode());
    }

    @Test
    public void testShouldSetWithSubCentreCode() {
        frontLineWorkerCsv.setSubCentreCode("12345");
        assertEquals("12345", frontLineWorkerCsv.getSubCentreCode());
    }

    @Test
    public void testShouldSetWithVillageCode() {
        frontLineWorkerCsv.setVillageCode("1234");
        assertEquals("1234", frontLineWorkerCsv.getVillageCode());
    }

    @Test
    public void testShouldSetWithAshaNumber() {
        frontLineWorkerCsv.setAshaNumber("9876");
        assertEquals("9876", frontLineWorkerCsv.getAshaNumber());
    }

    @Test
    public void testShouldSetWithAdhaarNo() {
        frontLineWorkerCsv.setAdhaarNo("1234");
        assertEquals("1234", frontLineWorkerCsv.getAdhaarNo());
    }

    @Test
    public void testShouldSetWithIsValid() {
        frontLineWorkerCsv.setIsValid("True");
        assertEquals("True", frontLineWorkerCsv.getIsValid());
    }

    @Test
    public void testShouldSetWithNmsFlwId() {
        frontLineWorkerCsv.setSystemGeneratedFlwId("null");
        assertEquals("null", frontLineWorkerCsv.getSystemGeneratedFlwId());
    }

    @Test
    public void testShouldSetWithHealthBlockCode() {
        frontLineWorkerCsv.setHealthBlockCode("1234");
        assertEquals("1234", frontLineWorkerCsv.getHealthBlockCode());
    }

    @Test
    public void testShouldSetWithDistrictCode() {
        frontLineWorkerCsv.setDistrictCode("1234");
        assertEquals("1234", frontLineWorkerCsv.getDistrictCode());
    }

    @Test
    public void testShouldSetWithStateCode() {
        frontLineWorkerCsv.setStateCode("12");
        assertEquals("12", frontLineWorkerCsv.getStateCode());
    }


}

