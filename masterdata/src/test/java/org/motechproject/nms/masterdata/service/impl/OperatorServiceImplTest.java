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

        Operator operatorData =getOperatorData();

        when(operatorDataService.create(operatorData)).thenReturn(operatorData);

        operatorService.create(operatorData);
    }

    @Test
    public void testUpdate() throws Exception {

        Operator operatorData = getOperatorData();

        when(operatorDataService.update(operatorData)).thenReturn(operatorData);

        operatorService.update(operatorData);
    }

    public void testDelete() throws Exception {

        Operator operatorData = getOperatorData();

        doNothing().when(operatorDataService).delete(operatorData);

        operatorService.delete(operatorData);
    }

    public void testGetRecordByCode() throws Exception {

        Operator operatorData = getOperatorData();

        when(operatorDataService.findByCode("anyString")).thenReturn(operatorData);

        assertNotNull(operatorService.getRecordByCode("anyString"));
        assertTrue("Test".equals(operatorService.getRecordByCode("anyString").getName()));
    }

    private Operator getOperatorData() {
        Operator operatorData = new Operator();
        operatorData.setId(1L);
        operatorData.setName("Test");
        operatorData.setCode("123");
        return operatorData;
    }
}