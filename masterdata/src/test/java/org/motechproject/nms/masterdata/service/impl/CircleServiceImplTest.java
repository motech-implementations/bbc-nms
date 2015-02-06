package org.motechproject.nms.masterdata.service.impl;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.repository.CircleDataService;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by abhishek on 6/2/15.
 */
public class CircleServiceImplTest extends TestCase {

    private CircleServiceImpl circleService;

    @Mock
    private CircleDataService circleDataService;

    @Before
    public void setUp() {
        initMocks(this);
        this.circleService = new CircleServiceImpl(circleDataService);
    }

    @Test
    public void testGetRecordByCode() throws Exception {

        Circle circleData = getCircleData();

        when(circleDataService.findByCode("anyString")).thenReturn(circleData);

        assertNotNull(circleService.getRecordByCode("anyString"));
        assertTrue("Test".equals(circleService.getRecordByCode("anyString").getName()));
    }

    private Circle getCircleData() {
        Circle circleData = new Circle();
        circleData.setId(1L);
        circleData.setName("Test");
        circleData.setCode("123");
        return circleData;
    }
}