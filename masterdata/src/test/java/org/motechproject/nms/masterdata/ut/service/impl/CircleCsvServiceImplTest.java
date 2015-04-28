package org.motechproject.nms.masterdata.ut.service.impl;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.nms.masterdata.domain.CsvCircle;
import org.motechproject.nms.masterdata.repository.CircleCsvDataService;
import org.motechproject.nms.masterdata.service.impl.CircleCsvServiceImpl;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class is used to test(UT) the operations of Circle Csv Implementations
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

    @Test
    public void testGetRecordByCode() throws Exception {

        CsvCircle csvCircleData = getCircleCsvData();

        when(circleCsvDataService.findById(1L)).thenReturn(csvCircleData);

        assertNotNull(circleCsvService.getRecord(1L));
        assertTrue("Test".equals(circleCsvService.getRecord(1L).getName()));
    }

    private CsvCircle getCircleCsvData() {
        CsvCircle csvCircleData = new CsvCircle();
        csvCircleData.setId(1L);
        csvCircleData.setName("Test");
        csvCircleData.setCode("123");
        return csvCircleData;
    }
}