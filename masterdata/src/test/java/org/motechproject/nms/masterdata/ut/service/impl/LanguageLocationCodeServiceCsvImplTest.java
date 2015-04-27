package org.motechproject.nms.masterdata.ut.service.impl;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.nms.masterdata.domain.CsvLanguageLocationCode;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeCsvDataService;
import org.motechproject.nms.masterdata.service.impl.LanguageLocationCodeCsvServiceImpl;

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
        CsvLanguageLocationCode csvLanguageLocationCodeData = new CsvLanguageLocationCode();
        csvLanguageLocationCodeData.setId(1L);

        when(languageLocationCodeCsvDataService.findById(1L)).thenReturn(csvLanguageLocationCodeData);

        assertNotNull(languageLocationCodeServiceCsv.getRecord(1L));
        assertTrue(1L == languageLocationCodeServiceCsv.getRecord(1L).getId());
    }
}
