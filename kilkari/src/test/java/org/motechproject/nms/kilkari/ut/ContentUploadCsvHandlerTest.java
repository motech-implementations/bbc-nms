package org.motechproject.nms.kilkari.ut;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.nms.kilkari.domain.ContentUpload;
import org.motechproject.nms.kilkari.domain.ContentUploadCsv;
import org.motechproject.nms.kilkari.event.handler.ContentUploadCsvHandler;
import org.motechproject.nms.kilkari.service.ContentUploadCsvService;
import org.motechproject.nms.kilkari.service.ContentUploadService;
import org.motechproject.nms.masterdata.domain.Circle;
import org.motechproject.nms.masterdata.domain.LanguageLocationCode;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.service.BulkUploadErrLogService;

import java.lang.reflect.Method;

import static org.mockito.MockitoAnnotations.initMocks;

public class ContentUploadCsvHandlerTest extends TestCase {

    @Mock
    private CircleService circleService;

    @Mock
    private LanguageLocationCodeService languageLocationCodeService;

    @Mock
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Mock
    private ContentUploadService contentUploadService;

    @Mock
    private ContentUploadCsvService contentUploadCsvService;

    private ContentUploadCsvHandler handler;

    @Before
    public void init() {
        initMocks(this);
        this.handler = new ContentUploadCsvHandler(bulkUploadErrLogService, contentUploadService,
                contentUploadCsvService, circleService, languageLocationCodeService);
    }

    @Test
    public void testDataValidationExceptionShouldRaisedIfCircleIsNull() {
        init();

        ContentUpload record = new ContentUpload();

        ContentUploadCsv csv = new ContentUploadCsv();
        csv.setCircleCode("testCode");

        Method method = null;
        try {
            method = handler.getClass().getDeclaredMethod("mapContentUploadFrom", ContentUploadCsv.class);
            method.setAccessible(true);
            record = (ContentUpload)method.invoke(handler, csv);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }catch(Exception e) {
            Assert.assertTrue(e.getCause() instanceof DataValidationException);
        }
    }

    @Test
    public void testDataValidationExceptionShouldRaisedIfLanguageLocationCodeIsNull() {
        init();

        ContentUpload record = new ContentUpload();
        Circle circle = new Circle();
        circle.setCode("testCode");

        ContentUploadCsv csv = new ContentUploadCsv();
        csv.setCircleCode("testCode");
        csv.setLanguageLocationCode("123");
        Mockito.when(circleService.getRecordByCode("testCode")).thenReturn(circle);

        Method method = null;
        try {
            method = handler.getClass().getDeclaredMethod("mapContentUploadFrom", ContentUploadCsv.class);
            method.setAccessible(true);
            record = (ContentUpload)method.invoke(handler, csv);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }catch(Exception e) {
            Assert.assertTrue(e.getCause() instanceof DataValidationException);
        }
    }

    @Test
    public void testMethodIfContentTypeIsCONTENT() {
        init();

        ContentUpload record = new ContentUpload();
        Circle circle = new Circle();
        circle.setCode("testCode");

        LanguageLocationCode llc = new LanguageLocationCode();

        ContentUploadCsv csv = new ContentUploadCsv();
        csv.setCircleCode("testCode");
        csv.setLanguageLocationCode("123");
        csv.setContentType("CONTENT");
        csv.setContentDuration("10");
        csv.setContentFile("file");
        csv.setContentId("12");
        csv.setContentName("name");
        Mockito.when(circleService.getRecordByCode("testCode")).thenReturn(circle);
        Mockito.when(languageLocationCodeService.getRecordByCircleCodeAndLangLocCode("testCode", 123)).thenReturn(llc);

        Method method = null;
        try {
            method = handler.getClass().getDeclaredMethod("mapContentUploadFrom", ContentUploadCsv.class);
            method.setAccessible(true);
            record = (ContentUpload)method.invoke(handler, csv);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }catch(Exception e) {
            Assert.assertTrue(e.getCause() instanceof DataValidationException);
        }
    }
}
