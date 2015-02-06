package org.motechproject.nms.masterdata.service.impl;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.masterdata.repository.OperatorDataService;
import org.motechproject.nms.masterdata.service.OperatorService;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OperatorServiceImplTest extends TestCase {

    private OperatorService operatorService;

    @Mock
    private OperatorDataService operatorDataService;

    @Before
    public void setUp() {
        initMocks(this);
        this.operatorService = new OperatorServiceImpl(operatorDataService);
    }

    @Test
    public void testCreate() throws Exception {

        Operator operatorData = new Operator();
        operatorData.setId(1L);
        operatorData.setName("Test");
        operatorData.setCode("123");

        when(operatorDataService.findById(1L)).thenReturn(operatorData);
        operatorService.create(operatorData);
    }

    @Test
    public void testUpdate() throws Exception {

        Operator operatorData = new Operator();
        operatorData.setId(1L);
        operatorData.setName("Test");
        operatorData.setCode("123");

        operatorService.create(operatorData);

        when(operatorDataService.findById(1L)).thenReturn(operatorData);

        operatorData.setName("Test1");
        operatorService.update(operatorData);

        assertTrue("Test1".equals(operatorDataService.findById(1L).getName()));
    }

    public void testDelete() throws Exception {
        Operator operatorData = new Operator();
        operatorData.setId(1L);
        operatorData.setName("Test");
        operatorData.setCode("123");

        doNothing().when(operatorDataService).delete(operatorData);
        operatorService.delete(operatorData);
    }

    public void testGetRecordByCode() throws Exception {

        Operator operatorData = new Operator();
        operatorData.setId(1L);
        operatorData.setName("TestName");
        operatorData.setCode("123");

        when(operatorDataService.findByCode("anyString")).thenReturn(operatorData);

        assertNotNull(operatorService.getRecordByCode("anyString"));
        assertTrue("TestName".equals(operatorService.getRecordByCode("anyString").getName()));
    }
}