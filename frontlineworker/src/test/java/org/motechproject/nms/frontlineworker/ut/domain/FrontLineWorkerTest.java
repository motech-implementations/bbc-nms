package org.motechproject.nms.frontlineworker.ut.domain;

import org.junit.Test;
import org.motechproject.nms.frontlineworker.Designation;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class performs the UT of FrontLineWorker.
 */
public class FrontLineWorkerTest {

    FrontLineWorker frontLineWorker = new FrontLineWorker();

    @Test
    public void testFlwId() {
        frontLineWorker.setFlwId(1L);
        assertTrue(1L == frontLineWorker.getFlwId());
    }

    @Test
    public void testContactNo() {
        frontLineWorker.setContactNo("9999099990");
        assertEquals("9999099990", frontLineWorker.getContactNo());
    }

    @Test
    public void testName() {
        frontLineWorker.setName("Etasha");
        assertEquals("Etasha", frontLineWorker.getName());
    }

    @Test
    public void testDesignation() {
        frontLineWorker.setDesignation(Designation.ASHA);
        assertEquals(Designation.ASHA, frontLineWorker.getDesignation());
    }

    @Test
    public void testOperatorId() {
        frontLineWorker.setOperatorId(123L);
        assertTrue(123L == frontLineWorker.getOperatorId());
    }

    @Test
    public void testStateCode() {
        frontLineWorker.setStateCode(12L);
        assertTrue(12L == frontLineWorker.getStateCode());
    }

    @Test
    public void AshaNumber() {
        frontLineWorker.setAshaNumber("9876");
        assertEquals("9876", frontLineWorker.getAshaNumber());
    }

    @Test
    public void testAdhaarNumber() {
        frontLineWorker.setAdhaarNumber("1234");
        assertEquals("1234", frontLineWorker.getAdhaarNumber());
    }

    @Test
    public void testStatus() {
        frontLineWorker.setStatus(Status.INACTIVE);
        assertEquals(Status.INACTIVE, frontLineWorker.getStatus());
    }

    @Test
    public void testLanguageLocationCodeId() {
        frontLineWorker.setLanguageLocationCodeId(123);
        assertTrue(123 == frontLineWorker.getLanguageLocationCodeId());
    }

    @Test
    public void testDefaultLanguageLocationCodeId() {
        frontLineWorker.setDefaultLanguageLocationCodeId(123);
        assertTrue(123 == frontLineWorker.getDefaultLanguageLocationCodeId());
    }

}
