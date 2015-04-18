package org.motechproject.nms.mobilekunji.ut.domain;


import org.junit.Before;
import org.junit.Test;
import org.motechproject.nms.mobilekunji.domain.CallDetail;

import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class is used to test the CallDetail Domain Class.
 */
public class CallDetailTest {

    private CallDetail callDetail;

    @Before
    public void init() {
        initMocks(this);
        callDetail = new CallDetail("111111111111111", "9810179788", "B", 1, "SUCC", "DL", 29L, 12L, 34L);

    }

    @Test
    public void TestCardDetail() {

        callDetail.setCardDetail(null);
        assertNotNull(callDetail);
        assertNotNull(callDetail.getCardDetail());
        assertNotNull(callDetail.getCardDetail().size() == 0);
    }
}
