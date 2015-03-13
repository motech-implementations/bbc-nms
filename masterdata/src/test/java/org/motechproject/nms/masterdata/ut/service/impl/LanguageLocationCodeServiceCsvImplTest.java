package org.motechproject.nms.masterdata.service.impl;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.nms.masterdata.domain.LanguageLocationCodeCsv;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeCsvDataService;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class is used to test(UT) the operations of Language Location Code Csv Implementations
 */
public class LanguageLocationCodeServiceCsvImplTest extends TestCase {

    private LanguageLocationCodeCsvServiceImpl languageLocationCodeServiceCsv;

    @Mock
    private LanguageLocationCodeCsvDataService languageLocationCodeCsvDataService;

    @Before
    public void setUp() {
        initMocks(this);
        this.languageLocationCodeServiceCsv = new LanguageLocationCodeCsvServiceImpl(languageLocationCodeCsvDataService);
    }

    @Test
    public void testGetRecord() {
        LanguageLocationCodeCsv languageLocationCodeCsvData = new LanguageLocationCodeCsv();
        languageLocationCodeCsvData.setId(1L);

        when(languageLocationCodeCsvDataService.findById(1L)).thenReturn(languageLocationCodeCsvData);

        assertNotNull(languageLocationCodeServiceCsv.getRecord(1L));
        assertTrue(1L == languageLocationCodeServiceCsv.getRecord(1L).getId());
    }
}
