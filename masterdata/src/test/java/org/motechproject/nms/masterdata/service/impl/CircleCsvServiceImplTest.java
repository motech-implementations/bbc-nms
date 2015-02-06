package org.motechproject.nms.masterdata.service.impl;

import junit.framework.TestCase;
import org.junit.Before;
import org.mockito.Mock;
import org.motechproject.nms.masterdata.domain.CircleCsv;
import org.motechproject.nms.masterdata.repository.CircleCsvDataService;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by abhishek on 6/2/15.
 */
public class CircleCsvServiceImplTest extends TestCase {

    private CircleCsvServiceImpl circleCsvService;

    @Mock
    private CircleCsvDataService circleCsvDataService;

    @Before
    public void setUp() {
        initMocks(this);
        this.circleCsvService = new CircleCsvServiceImpl(circleCsvDataService);
    }

    public void testDelete() throws Exception {

        CircleCsv circleCsvData = getCircleCsvData();

        doNothing().when(circleCsvDataService).delete(circleCsvData);

        circleCsvService.delete(circleCsvData);
    }

    public void testGetRecordByCode() throws Exception {

        CircleCsv circleCsvData = getCircleCsvData();

        when(circleCsvDataService.findById(1L)).thenReturn(circleCsvData);

        assertNotNull(circleCsvService.getRecord(1L));
        assertTrue("Test".equals(circleCsvService.getRecord(1L).getName()));
    }

    private CircleCsv getCircleCsvData() {
        CircleCsv circleCsvData = new CircleCsv();
        circleCsvData.setId(1L);
        circleCsvData.setName("Test");
        circleCsvData.setCode("123");
        return circleCsvData;
    }
}