package org.motechproject.nms.masterdata.ut.service.impl;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.mds.query.QueryExecution;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeDataService;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.impl.LanguageLocationCodeServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class is used to test(UT) the operations of Language Location Code Implementations
 */
public class LanguageLocationCodeServiceImplTest extends TestCase {

    private LanguageLocationCodeService languageLocationCodeService;

    @Mock
    private LanguageLocationCodeDataService languageLocationCodeDataService;

    @Mock
    private CircleService circleService;

    @Before
    public void setUp() {
        initMocks(this);
        this.languageLocationCodeService = new LanguageLocationCodeServiceImpl(languageLocationCodeDataService, circleService);
    }

    @Test
    public void testGetRecordByLocationCode() {

        LanguageLocationCode languageLocationCodeData = new LanguageLocationCode();
        languageLocationCodeData.setId(1L);
        languageLocationCodeData.setStateCode(123L);
        languageLocationCodeData.setDistrictCode(345L);
        languageLocationCodeData.setLanguageLocationCode("100");

        when(languageLocationCodeDataService.findByLocationCode(123L, 345L)).thenReturn(languageLocationCodeData);

        assertNotNull(languageLocationCodeService.getRecordByLocationCode(123L, 345L));
        assertTrue(123L == languageLocationCodeService.getRecordByLocationCode(123L, 345L).getStateCode());
        assertTrue(345L == languageLocationCodeService.getRecordByLocationCode(123L, 345L).getDistrictCode());
    }

    @Test
    public void testGetLanguageLocationCodeByLocationCode() {

        LanguageLocationCode languageLocationCodeData = new LanguageLocationCode();
        languageLocationCodeData.setId(1L);
        languageLocationCodeData.setStateCode(123L);
        languageLocationCodeData.setDistrictCode(345L);
        languageLocationCodeData.setLanguageLocationCode("100");
        LanguageLocationCodeServiceImpl languageLocationCodeServiceImpl = new LanguageLocationCodeServiceImpl(languageLocationCodeDataService, circleService);

        when(languageLocationCodeDataService.findByLocationCode(123L, 345L)).thenReturn(languageLocationCodeData);

        assertNotNull(languageLocationCodeService.getRecordByLocationCode(123L, 345L).getLanguageLocationCode());
        assertTrue("100".equals(languageLocationCodeService.getRecordByLocationCode(123L, 345L).getLanguageLocationCode()));
        assertTrue(languageLocationCodeData.getLanguageLocationCode().equals(languageLocationCodeServiceImpl.getLanguageLocationCodeByLocationCode(languageLocationCodeData.getStateCode(), languageLocationCodeData.getDistrictCode())));
        assertTrue("100".equals(languageLocationCodeServiceImpl.getLanguageLocationCodeByLocationCode(languageLocationCodeData.getStateCode(), languageLocationCodeData.getDistrictCode())));

    }

    @Test
    public void testGetDefaultLanguageLocationCodeByCircleCode() {

        Circle circleData = new Circle();
        circleData.setId(1L);
        circleData.setDefaultLanguageLocationCode("100");

        when(circleService.getRecordByCode("anyString")).thenReturn(circleData);

        assertNotNull(languageLocationCodeService.getDefaultLanguageLocationCodeByCircleCode("anyString"));
        assertTrue("100".equals(languageLocationCodeService.getDefaultLanguageLocationCodeByCircleCode("anyString")));
    }

    @Test
    public void testGetLanguageLocationCodeByCircleCode() {

        List<String> listData = new ArrayList<String>();
        listData.add("100");

        when(languageLocationCodeDataService.executeQuery(any(QueryExecution.class))).thenReturn(listData);

        assertNotNull(languageLocationCodeService.getLanguageLocationCodeByCircleCode("anyString"));
        assertTrue("100".equals(languageLocationCodeService.getLanguageLocationCodeByCircleCode("anyString")));
    }

    @Test
    public void testExecuteUniqueLanguageLocationCodeQuery() {
        LanguageLocationCode languageLocationCodeData = new LanguageLocationCode();
        languageLocationCodeData.setId(1L);
        languageLocationCodeData.setStateCode(123L);
        languageLocationCodeData.setDistrictCode(345L);
        languageLocationCodeData.setLanguageLocationCode("100");
        LanguageLocationCodeServiceImpl languageLocationCodeServiceImpl = new LanguageLocationCodeServiceImpl(languageLocationCodeDataService, circleService);

        assertNull(languageLocationCodeServiceImpl.getLanguageLocationCodeByCircleCode(languageLocationCodeData.getCircleCode()));
    }


}
