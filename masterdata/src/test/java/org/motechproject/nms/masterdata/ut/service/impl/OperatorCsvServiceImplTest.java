package org.motechproject.nms.masterdata.ut.service.impl;

import junit.framework.TestCase;
import org.junit.Before;
import org.mockito.Mock;
import org.motechproject.nms.masterdata.domain.CsvOperator;
import org.motechproject.nms.masterdata.repository.OperatorCsvDataService;
import org.motechproject.nms.masterdata.service.OperatorCsvService;
import org.motechproject.nms.masterdata.service.impl.OperatorCsvServiceImpl;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class is used to test(UT) the operations of Operator Csv Implementations
 */
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

        CsvOperator csvOperatorData = new CsvOperator();
        csvOperatorData.setId(1L);
        csvOperatorData.setName("Test");
        csvOperatorData.setCode("123");

        when(operatorCsvDataService.findById(1L)).thenReturn(csvOperatorData);
        assertNotNull(operatorCsvService.getRecord(1L));
        assertTrue("Test".equals(operatorCsvService.getRecord(1L).getName()));
        assertTrue("123".equals(operatorCsvService.getRecord(1L).getCode()));
    }

}