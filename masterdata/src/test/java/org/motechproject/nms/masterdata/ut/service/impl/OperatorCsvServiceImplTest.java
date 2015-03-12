package org.motechproject.nms.masterdata.service.impl;

import junit.framework.TestCase;
import org.junit.Before;
import org.mockito.Mock;
import org.motechproject.nms.masterdata.domain.OperatorCsv;
import org.motechproject.nms.masterdata.repository.OperatorCsvDataService;
import org.motechproject.nms.masterdata.service.OperatorCsvService;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OperatorCsvServiceImplTest extends TestCase {

    private OperatorCsvService operatorCsvService;

    @Mock
    private OperatorCsvDataService operatorCsvDataService;

    @Before
    public void setUp() {
        initMocks(this);
        this.operatorCsvService = new OperatorCsvServiceImpl(operatorCsvDataService);
    }

    public void testGetRecord() throws Exception {

        OperatorCsv operatorCsvData = new OperatorCsv();
        operatorCsvData.setId(1L);
        operatorCsvData.setName("Test");
        operatorCsvData.setCode("123");

        when(operatorCsvDataService.findById(1L)).thenReturn(operatorCsvData);
        assertNotNull(operatorCsvService.getRecord(1L));
        assertTrue("Test".equals(operatorCsvService.getRecord(1L).getName()));
        assertTrue("123".equals(operatorCsvService.getRecord(1L).getCode()));
    }

}