package org.motechproject.nms.frontlineworker.ut.domain;

import org.junit.Test;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;

import static org.junit.Assert.assertEquals;

/**
 * Created by abhishek on 12/3/15.
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

}
