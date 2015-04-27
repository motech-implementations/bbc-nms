package org.motechproject.nms.masterdata.ut.event.handler;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.event.handler.LanguageLocationCodeCsvHandler;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeServiceCsv;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.service.BulkUploadErrLogService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class is used to test(UT) the operations of Language Location Code Csv
 */
public class LanguageLocationCodeCsvHandlerTest extends TestCase {

    private LanguageLocationCodeCsvHandler handler;

    @Mock
    private LanguageLocationCodeService languageLocationCodeService;

    @Mock
    private LanguageLocationCodeServiceCsv languageLocationCodeServiceCsv;

    @Mock
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Mock
    private CircleService circleService;

    @Mock
    private LocationService locationService;

    List<Long> createdIds = new ArrayList<Long>();

    @Before
    public void init() {
        initMocks(this);
        this.handler = new LanguageLocationCodeCsvHandler(languageLocationCodeService, languageLocationCodeServiceCsv,
                bulkUploadErrLogService, circleService, locationService);
    }

    @Test
    public void testDataValidationExceptionShouldRaisedIfLongParsingFailed() {
        init();
        LanguageLocationCode record = new LanguageLocationCode();

        CsvLanguageLocationCode csv = new CsvLanguageLocationCode();
        csv.setStateCode("");

        Method method = null;
        try {
            method = handler.getClass().getDeclaredMethod("mapLanguageLocationCodeFrom", CsvLanguageLocationCode.class);
            method.setAccessible(true);
            record = (LanguageLocationCode) method.invoke(handler, csv);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Assert.assertTrue(e.getCause() instanceof DataValidationException);
        }

    }

    @Test
    public void testDataValidationExceptionShouldRaisedIfStateIsNull() {
        init();
        LanguageLocationCode record = new LanguageLocationCode();

        CsvLanguageLocationCode csv = new CsvLanguageLocationCode();
        csv.setStateCode("1");
        csv.setDistrictCode("1");
        csv.setCircleCode("testCode");

        Method method = null;
        try {
            method = handler.getClass().getDeclaredMethod("mapLanguageLocationCodeFrom", CsvLanguageLocationCode.class);
            method.setAccessible(true);
            record = (LanguageLocationCode) method.invoke(handler, csv);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Assert.assertTrue(e.getCause() instanceof DataValidationException);
        }

    }

    @Test
    public void testDataValidationExceptionShouldRaisedIfDistrictIsNull() {
        init();
        LanguageLocationCode record = new LanguageLocationCode();
        State state = new State();
        state.setStateCode(1L);
        Mockito.when(locationService.getStateByCode(1L)).thenReturn(state);

        CsvLanguageLocationCode csv = new CsvLanguageLocationCode();
        csv.setStateCode("1");
        csv.setDistrictCode("1");
        csv.setCircleCode("testCode");

        Method method = null;
        try {
            method = handler.getClass().getDeclaredMethod("mapLanguageLocationCodeFrom", CsvLanguageLocationCode.class);
            method.setAccessible(true);
            record = (LanguageLocationCode) method.invoke(handler, csv);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Assert.assertTrue(e.getCause() instanceof DataValidationException);
        }

    }

    @Test
    public void testDataValidationExceptionShouldRaisedIfCircleIsNull() {
        init();
        LanguageLocationCode record = new LanguageLocationCode();
        State state = new State();
        state.setStateCode(1L);
        state.setId(1L);

        District district = new District();
        district.setStateCode(1L);

        Mockito.when(locationService.getStateByCode(1L)).thenReturn(state);
        Mockito.when(locationService.getDistrictByCode(1L, 1L)).thenReturn(district);

        CsvLanguageLocationCode csv = new CsvLanguageLocationCode();
        csv.setStateCode("1");
        csv.setDistrictCode("1");
        csv.setCircleCode("testCode");

        Method method = null;
        try {
            method = handler.getClass().getDeclaredMethod("mapLanguageLocationCodeFrom", CsvLanguageLocationCode.class);
            method.setAccessible(true);
            record = (LanguageLocationCode) method.invoke(handler, csv);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Assert.assertTrue(e.getCause() instanceof DataValidationException);
        }
    }

    @Test
    public void testIfIsDefaultLangLocCodeIsNull() {
        init();
        LanguageLocationCode record = new LanguageLocationCode();
        State state = new State();
        state.setStateCode(1L);
        state.setId(1L);

        District district = new District();
        district.setStateCode(1L);

        Circle circle = new Circle();
        circle.setCode("testCode");

        Mockito.when(locationService.getStateByCode(1L)).thenReturn(state);
        Mockito.when(locationService.getDistrictByCode(1L, 1L)).thenReturn(district);
        Mockito.when(circleService.getRecordByCode("testCode")).thenReturn(circle);

        CsvLanguageLocationCode csv = new CsvLanguageLocationCode();
        csv.setStateCode("1");
        csv.setDistrictCode("1");
        csv.setCircleCode("testCode");
        csv.setLanguageLocationCode("123");
        csv.setLanguageKK("KK");
        csv.setLanguageMA("MA");
        csv.setLanguageMK("MK");
        csv.setIsDefaultLanguageLocationCode("NNNN");

        Method method = null;
        try {
            method = handler.getClass().getDeclaredMethod("mapLanguageLocationCodeFrom", CsvLanguageLocationCode.class);
            method.setAccessible(true);
            record = (LanguageLocationCode) method.invoke(handler, csv);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Assert.assertTrue(e.getCause() instanceof DataValidationException);
        }
    }
}
