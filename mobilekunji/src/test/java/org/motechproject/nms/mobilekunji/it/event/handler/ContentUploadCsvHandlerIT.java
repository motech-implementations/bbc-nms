package org.motechproject.nms.mobilekunji.it.event.handler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.mobilekunji.domain.ContentType;
import org.motechproject.nms.mobilekunji.domain.ContentUpload;
import org.motechproject.nms.mobilekunji.domain.CsvContentUpload;
import org.motechproject.nms.mobilekunji.event.handler.ContentUploadCsvHandler;
import org.motechproject.nms.mobilekunji.service.ContentUploadCsvService;
import org.motechproject.nms.mobilekunji.service.ContentUploadService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * This class is used to test(IT) the operations of ContentUploadCsvHandler
 */

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ContentUploadCsvHandlerIT extends BasePaxIT {

    @Inject
    private ContentUploadCsvService contentUploadCsvService;

    @Inject
    private ContentUploadService contentUploadService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    private ContentUploadCsvHandler contentUploadCsvHandler;

    @Before
    public void setUp() {


        contentUploadCsvHandler = new ContentUploadCsvHandler(bulkUploadErrLogService, contentUploadService,
                contentUploadCsvService);

        assertNotNull(contentUploadCsvService);
        assertNotNull(contentUploadService);
        assertNotNull(bulkUploadErrLogService);
    }


    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void testContentUploadValidData() {

        CsvContentUpload csvContentUpload1 = new CsvContentUpload();
        csvContentUpload1.setIndex(1L);
        csvContentUpload1.setContentId("12");
        csvContentUpload1.setCircleCode("CircleCode");
        csvContentUpload1.setLanguageLocationCode("123");
        csvContentUpload1.setContentName("Content");
        csvContentUpload1.setContentType("PROMPT");
        csvContentUpload1.setContentFile("NewFile");
        csvContentUpload1.setCardCode("10");
        csvContentUpload1.setContentDuration("120");
        csvContentUpload1.setModifiedBy("Etasha");
        csvContentUpload1.setOwner("Etasha");
        csvContentUpload1.setCreator("Etasha");

        CsvContentUpload csvContentUpload2 = new CsvContentUpload();
        csvContentUpload2.setIndex(2L);
        csvContentUpload2.setContentId("22");
        csvContentUpload2.setCircleCode("CircleCode");
        csvContentUpload2.setLanguageLocationCode("123");
        csvContentUpload2.setContentName("Content");
        csvContentUpload2.setContentType("CONTENT");
        csvContentUpload2.setContentFile("NewFile");
        csvContentUpload2.setCardCode("11");
        csvContentUpload2.setContentDuration("120");
        csvContentUpload2.setModifiedBy("Etasha");
        csvContentUpload2.setOwner("Etasha");
        csvContentUpload2.setCreator("Etasha");

        CsvContentUpload csvContentUpload3 = new CsvContentUpload();

        csvContentUpload3.setIndex(3L);
        csvContentUpload3.setContentId("33");
        csvContentUpload3.setCircleCode("CircleCode");
        csvContentUpload3.setLanguageLocationCode("123");
        csvContentUpload3.setContentName("Content");
        csvContentUpload3.setContentType("CONTENTS");
        csvContentUpload3.setContentFile("NewFile");
        csvContentUpload3.setCardCode("12");
        csvContentUpload3.setContentDuration("120");
        csvContentUpload3.setModifiedBy("Etasha");
        csvContentUpload3.setOwner("Etasha");
        csvContentUpload3.setCreator("Etasha");


        CsvContentUpload csvContentUpload4 = new CsvContentUpload();
        csvContentUpload4.setIndex(4L);
        csvContentUpload4.setContentId("22");
        csvContentUpload4.setCircleCode("CircleCode");
        csvContentUpload4.setLanguageLocationCode("124");
        csvContentUpload4.setContentName("Content");
        csvContentUpload4.setContentType("CONTENT");
        csvContentUpload4.setContentFile("NewFile");
        csvContentUpload4.setCardCode("10");
        csvContentUpload4.setContentDuration("120");
        csvContentUpload4.setModifiedBy("Etasha");
        csvContentUpload4.setOwner("Etasha");
        csvContentUpload4.setCreator("Etasha");

        CsvContentUpload csvContentUploadDb1 = contentUploadCsvService.createContentUploadCsv(csvContentUpload1);
        CsvContentUpload csvContentUploadDb2 = contentUploadCsvService.createContentUploadCsv(csvContentUpload2);
        CsvContentUpload csvContentUploadDb3 = contentUploadCsvService.createContentUploadCsv(csvContentUpload3);
        CsvContentUpload csvContentUploadDb4 = contentUploadCsvService.createContentUploadCsv(csvContentUpload4);
        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(csvContentUploadDb1.getId());
        uploadedIds.add(csvContentUploadDb2.getId());
        uploadedIds.add(csvContentUploadDb3.getId());
        uploadedIds.add(csvContentUploadDb4.getId());

        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("CsvContentUpload.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        ContentUpload contentUpload = contentUploadService.findRecordByContentId(12);

        assertNotNull(contentUpload);


        assertTrue(12 == contentUpload.getContentId());
        assertEquals("CircleCode", contentUpload.getCircleCode());
        assertTrue("123".equals(contentUpload.getLanguageLocationCode()));
        assertEquals("Content", contentUpload.getContentName());
        assertEquals(ContentType.PROMPT, contentUpload.getContentType());
        assertEquals("NewFile", contentUpload.getContentFile());
        assertTrue("10".equals(contentUpload.getCardCode()));
        assertTrue(120 == contentUpload.getContentDuration());
        assertEquals("Etasha", contentUpload.getCreator());
        assertEquals("Etasha", contentUpload.getModifiedBy());
        assertEquals("Etasha", contentUpload.getOwner());

        List<CsvContentUpload> listCsvContentUpload = contentUploadCsvService.retrieveAllFromCsv();
        assertTrue(listCsvContentUpload.size() == 0);


    }


    @Test
    public void testContentUploadNoContentId() throws DataValidationException {

        CsvContentUpload csvContentUpload = new CsvContentUpload();
        csvContentUpload.setIndex(1L);
        csvContentUpload.setCircleCode("CircleCode");
        csvContentUpload.setLanguageLocationCode("123");
        csvContentUpload.setContentName("Content");
        csvContentUpload.setContentType("PROMPT");
        csvContentUpload.setContentFile("NewFile");
        csvContentUpload.setCardCode("10");
        csvContentUpload.setContentDuration("120");
        csvContentUpload.setModifiedBy("Etasha");
        csvContentUpload.setOwner("Etasha");
        csvContentUpload.setCreator("Etasha");

        CsvContentUpload csvContentUploadDb = contentUploadCsvService.createContentUploadCsv(csvContentUpload);
        assertNotNull(contentUploadCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(csvContentUploadDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("CsvContentUpload.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<CsvContentUpload> listCsvContentUpload = contentUploadCsvService.retrieveAllFromCsv();
        assertTrue(listCsvContentUpload.size() == 0);
        throw new DataValidationException();

    }


    @Test
    public void testContentUploadNoCircleCode() throws DataValidationException {

        CsvContentUpload csvContentUpload = new CsvContentUpload();
        csvContentUpload.setIndex(1L);
        csvContentUpload.setContentId("13");
        csvContentUpload.setLanguageLocationCode("123");
        csvContentUpload.setContentName("Content");
        csvContentUpload.setContentType("PROMPT");
        csvContentUpload.setContentFile("NewFile");
        csvContentUpload.setCardCode("10");
        csvContentUpload.setContentDuration("120");
        csvContentUpload.setModifiedBy("Etasha");
        csvContentUpload.setOwner("Etasha");
        csvContentUpload.setCreator("Etasha");

        CsvContentUpload csvContentUploadDb = contentUploadCsvService.createContentUploadCsv(csvContentUpload);
        assertNotNull(contentUploadCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(csvContentUploadDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("CsvContentUpload.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<CsvContentUpload> listCsvContentUpload = contentUploadCsvService.retrieveAllFromCsv();
        assertTrue(listCsvContentUpload.size() == 0);
        throw new DataValidationException();

    }


    @Test
    public void testContentUploadNoLangLocCode() throws DataValidationException {

        CsvContentUpload csvContentUpload = new CsvContentUpload();
        csvContentUpload.setIndex(1L);
        csvContentUpload.setContentId("13");
        csvContentUpload.setCircleCode("CircleCode");
        csvContentUpload.setContentName("Content");
        csvContentUpload.setContentType("PROMPT");
        csvContentUpload.setContentFile("NewFile");
        csvContentUpload.setCardCode("10");
        csvContentUpload.setContentDuration("120");
        csvContentUpload.setModifiedBy("Etasha");
        csvContentUpload.setOwner("Etasha");
        csvContentUpload.setCreator("Etasha");

        CsvContentUpload csvContentUploadDb = contentUploadCsvService.createContentUploadCsv(csvContentUpload);
        assertNotNull(contentUploadCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(csvContentUploadDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("CsvContentUpload.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<CsvContentUpload> listCsvContentUpload = contentUploadCsvService.retrieveAllFromCsv();
        assertTrue(listCsvContentUpload.size() == 0);
        throw new DataValidationException();

    }


    @Test
    public void testContentUploadNoContentName() throws DataValidationException {

        CsvContentUpload csvContentUpload = new CsvContentUpload();
        csvContentUpload.setIndex(1L);
        csvContentUpload.setContentId("13");
        csvContentUpload.setCircleCode("CircleCode");
        csvContentUpload.setLanguageLocationCode("123");
        csvContentUpload.setContentType("PROMPT");
        csvContentUpload.setContentFile("NewFile");
        csvContentUpload.setCardCode("10");
        csvContentUpload.setContentDuration("120");
        csvContentUpload.setModifiedBy("Etasha");
        csvContentUpload.setOwner("Etasha");
        csvContentUpload.setCreator("Etasha");

        CsvContentUpload csvContentUploadDb = contentUploadCsvService.createContentUploadCsv(csvContentUpload);
        assertNotNull(contentUploadCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(csvContentUploadDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("CsvContentUpload.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<CsvContentUpload> listCsvContentUpload = contentUploadCsvService.retrieveAllFromCsv();
        assertTrue(listCsvContentUpload.size() == 0);
        throw new DataValidationException();

    }


    @Test
    public void testContentUploadNoContentType() throws DataValidationException {

        CsvContentUpload csvContentUpload = new CsvContentUpload();
        csvContentUpload.setIndex(1L);
        csvContentUpload.setContentId("13");
        csvContentUpload.setCircleCode("CircleCode");
        csvContentUpload.setLanguageLocationCode("123");
        csvContentUpload.setContentName("Content");
        csvContentUpload.setContentFile("NewFile");
        csvContentUpload.setCardCode("10");
        csvContentUpload.setContentDuration("120");
        csvContentUpload.setModifiedBy("Etasha");
        csvContentUpload.setOwner("Etasha");
        csvContentUpload.setCreator("Etasha");

        CsvContentUpload csvContentUploadDb = contentUploadCsvService.createContentUploadCsv(csvContentUpload);
        assertNotNull(contentUploadCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(csvContentUploadDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("CsvContentUpload.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<CsvContentUpload> listCsvContentUpload = contentUploadCsvService.retrieveAllFromCsv();
        assertTrue(listCsvContentUpload.size() == 0);
        throw new DataValidationException();

    }


    @Test
    public void testContentUploadNoContentFile() throws DataValidationException {

        CsvContentUpload csvContentUpload = new CsvContentUpload();
        csvContentUpload.setIndex(1L);
        csvContentUpload.setContentId("13");
        csvContentUpload.setCircleCode("CircleCode");
        csvContentUpload.setLanguageLocationCode("123");
        csvContentUpload.setContentName("Content");
        csvContentUpload.setContentType("PROMPT");
        csvContentUpload.setCardCode("10");
        csvContentUpload.setContentDuration("120");
        csvContentUpload.setModifiedBy("Etasha");
        csvContentUpload.setOwner("Etasha");
        csvContentUpload.setCreator("Etasha");

        CsvContentUpload csvContentUploadDb = contentUploadCsvService.createContentUploadCsv(csvContentUpload);
        assertNotNull(contentUploadCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(csvContentUploadDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("CsvContentUpload.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<CsvContentUpload> listCsvContentUpload = contentUploadCsvService.retrieveAllFromCsv();
        assertTrue(listCsvContentUpload.size() == 0);
        throw new DataValidationException();

    }

    @Test
    public void testContentUploadNoCardNumber() throws DataValidationException {

        CsvContentUpload csvContentUpload = new CsvContentUpload();
        csvContentUpload.setIndex(1L);
        csvContentUpload.setContentId("13");
        csvContentUpload.setCircleCode("CircleCode");
        csvContentUpload.setLanguageLocationCode("123");
        csvContentUpload.setContentName("Content");
        csvContentUpload.setContentType("PROMPT");
        csvContentUpload.setContentFile("NewFile");
        csvContentUpload.setContentDuration("120");
        csvContentUpload.setModifiedBy("Etasha");
        csvContentUpload.setOwner("Etasha");
        csvContentUpload.setCreator("Etasha");

        CsvContentUpload csvContentUploadDb = contentUploadCsvService.createContentUploadCsv(csvContentUpload);
        assertNotNull(contentUploadCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(csvContentUploadDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("CsvContentUpload.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<CsvContentUpload> listCsvContentUpload = contentUploadCsvService.retrieveAllFromCsv();
        assertTrue(listCsvContentUpload.size() == 0);
        throw new DataValidationException();

    }

    @Test
    public void testContentUploadInvalidCardNumber() throws DataValidationException {

        CsvContentUpload csvContentUpload = new CsvContentUpload();
        csvContentUpload.setIndex(1L);
        csvContentUpload.setContentId("13");
        csvContentUpload.setCircleCode("CircleCode");
        csvContentUpload.setLanguageLocationCode("123");
        csvContentUpload.setCardCode("222");
        csvContentUpload.setContentName("Content");
        csvContentUpload.setContentType("PROMPT");
        csvContentUpload.setContentFile("NewFile");
        csvContentUpload.setContentDuration("120");
        csvContentUpload.setModifiedBy("Etasha");
        csvContentUpload.setOwner("Etasha");
        csvContentUpload.setCreator("Etasha");

        CsvContentUpload csvContentUploadDb = contentUploadCsvService.createContentUploadCsv(csvContentUpload);
        assertNotNull(contentUploadCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(csvContentUploadDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("CsvContentUpload.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<CsvContentUpload> listCsvContentUpload = contentUploadCsvService.retrieveAllFromCsv();
        assertTrue(listCsvContentUpload.size() == 0);
        throw new DataValidationException();

    }

    @Test
    public void testContentUploadNoContentDuration() throws DataValidationException {

        CsvContentUpload csvContentUpload = new CsvContentUpload();
        csvContentUpload.setIndex(1L);
        csvContentUpload.setContentId("13");
        csvContentUpload.setCircleCode("CircleCode");
        csvContentUpload.setLanguageLocationCode("123");
        csvContentUpload.setContentName("Content");
        csvContentUpload.setContentType("PROMPT");
        csvContentUpload.setContentFile("NewFile");
        csvContentUpload.setCardCode("10");
        csvContentUpload.setModifiedBy("Etasha");
        csvContentUpload.setOwner("Etasha");
        csvContentUpload.setCreator("Etasha");

        CsvContentUpload csvContentUploadDb = contentUploadCsvService.createContentUploadCsv(csvContentUpload);
        assertNotNull(contentUploadCsvService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(csvContentUploadDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("CsvContentUpload.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<CsvContentUpload> listCsvContentUpload = contentUploadCsvService.retrieveAllFromCsv();
        assertTrue(listCsvContentUpload.size() == 0);
        throw new DataValidationException();

    }

}
