package org.motechproject.nms.mobilekunji.it.event.handler;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.mobilekunji.domain.ContentType;
import org.motechproject.nms.mobilekunji.domain.ContentUpload;
import org.motechproject.nms.mobilekunji.domain.ContentUploadCsv;
import org.motechproject.nms.mobilekunji.event.handler.ContentUploadCsvHandler;
import org.motechproject.nms.mobilekunji.repository.ContentUploadCsvRecordDataService;
import org.motechproject.nms.mobilekunji.repository.ContentUploadRecordDataService;
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
 * Created by abhishek on 4/3/15.
 */

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ContentUploadCsvHandlerIT extends BasePaxIT {

    @Inject
    private ContentUploadCsvRecordDataService contentUploadCsvRecordDataService;

    @Inject
    private ContentUploadRecordDataService contentUploadRecordDataService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    private ContentUploadCsvHandler contentUploadCsvHandler;

    @Before
    public void setUp() {


        contentUploadCsvHandler = new ContentUploadCsvHandler(contentUploadCsvRecordDataService,
                contentUploadRecordDataService, bulkUploadErrLogService );

        assertNotNull(contentUploadCsvRecordDataService);
        assertNotNull(contentUploadRecordDataService);
        assertNotNull(bulkUploadErrLogService);
    }


    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void testContentUploadValidData() {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("12");
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setLanguageLocationCode("123");
        contentUploadCsv.setContentName("Content");
        contentUploadCsv.setContentType("PROMPT");
        contentUploadCsv.setContentFile("NewFile");
        contentUploadCsv.setCardNumber("10");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setModifiedBy("Etasha");
        contentUploadCsv.setOwner("Etasha");
        contentUploadCsv.setCreator("Etasha");

        ContentUploadCsv contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(12);

        assertNotNull(contentUpload);


        assertTrue(12 == contentUpload.getContentId());
        assertEquals("CircleCode", contentUpload.getCircleCode());
        assertTrue(123 == contentUpload.getLanguageLocationCode());
        assertEquals("Content", contentUpload.getContentName());
        assertEquals(ContentType.PROMPT, contentUpload.getContentType());
        assertEquals("NewFile", contentUpload.getContentFile());
        assertTrue(10 == contentUpload.getCardNumber());
        assertTrue(120 == contentUpload.getContentDuration());
        assertEquals("Etasha", contentUpload.getCreator());
        assertEquals("Etasha", contentUpload.getModifiedBy());
        assertEquals("Etasha", contentUpload.getOwner());

        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);


    }




    @Test
    public void testContentUploadValidContentType() {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("12");
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setLanguageLocationCode("123");
        contentUploadCsv.setContentName("Content");
        contentUploadCsv.setContentType("CONTENT");
        contentUploadCsv.setContentFile("NewFile");
        contentUploadCsv.setCardNumber("10");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setModifiedBy("Etasha");
        contentUploadCsv.setOwner("Etasha");
        contentUploadCsv.setCreator("Etasha");

        ContentUploadCsv contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(12);

        assertNotNull(contentUpload);


        assertTrue(12 == contentUpload.getContentId());
        assertEquals("CircleCode", contentUpload.getCircleCode());
        assertTrue(123 == contentUpload.getLanguageLocationCode());
        assertEquals("Content", contentUpload.getContentName());
        assertEquals(ContentType.CONTENT, contentUpload.getContentType());
        assertEquals("NewFile", contentUpload.getContentFile());
        assertTrue(10 == contentUpload.getCardNumber());
        assertTrue(120 == contentUpload.getContentDuration());
        assertEquals("Etasha", contentUpload.getCreator());
        assertEquals("Etasha", contentUpload.getModifiedBy());
        assertEquals("Etasha", contentUpload.getOwner());

        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);


    }



    @Test
    public void testContentUploadInValidContentType() throws DataValidationException {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("12");
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setLanguageLocationCode("123");
        contentUploadCsv.setContentName("Content");
        contentUploadCsv.setContentType("PROMPTS");
        contentUploadCsv.setContentFile("NewFile");
        contentUploadCsv.setCardNumber("10");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setModifiedBy("Etasha");
        contentUploadCsv.setOwner("Etasha");
        contentUploadCsv.setCreator("Etasha");

        ContentUploadCsv contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(12);

        assertNull(contentUpload);
        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);
        throw new DataValidationException();


    }



    @Test
    public void testContentUploadNoContentId() throws DataValidationException {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setLanguageLocationCode("123");
        contentUploadCsv.setContentName("Content");
        contentUploadCsv.setContentType("PROMPT");
        contentUploadCsv.setContentFile("NewFile");
        contentUploadCsv.setCardNumber("10");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setModifiedBy("Etasha");
        contentUploadCsv.setOwner("Etasha");
        contentUploadCsv.setCreator("Etasha");

        ContentUploadCsv contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);
        assertNotNull(contentUploadCsvRecordDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);
        throw new DataValidationException();

    }


    @Test
    public void testContentUploadNoCircleCode() throws DataValidationException {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("13");
        contentUploadCsv.setLanguageLocationCode("123");
        contentUploadCsv.setContentName("Content");
        contentUploadCsv.setContentType("PROMPT");
        contentUploadCsv.setContentFile("NewFile");
        contentUploadCsv.setCardNumber("10");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setModifiedBy("Etasha");
        contentUploadCsv.setOwner("Etasha");
        contentUploadCsv.setCreator("Etasha");

        ContentUploadCsv contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);
        assertNotNull(contentUploadCsvRecordDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);
        throw new DataValidationException();

    }


    @Test
    public void testContentUploadNoLangLocCode() throws DataValidationException {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("13");
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setContentName("Content");
        contentUploadCsv.setContentType("PROMPT");
        contentUploadCsv.setContentFile("NewFile");
        contentUploadCsv.setCardNumber("10");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setModifiedBy("Etasha");
        contentUploadCsv.setOwner("Etasha");
        contentUploadCsv.setCreator("Etasha");

        ContentUploadCsv contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);
        assertNotNull(contentUploadCsvRecordDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);
        throw new DataValidationException();

    }


    @Test
    public void testContentUploadNoContentName() throws DataValidationException {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("13");
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setLanguageLocationCode("123");
        contentUploadCsv.setContentType("PROMPT");
        contentUploadCsv.setContentFile("NewFile");
        contentUploadCsv.setCardNumber("10");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setModifiedBy("Etasha");
        contentUploadCsv.setOwner("Etasha");
        contentUploadCsv.setCreator("Etasha");

        ContentUploadCsv contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);
        assertNotNull(contentUploadCsvRecordDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);
        throw new DataValidationException();

    }


    @Test
    public void testContentUploadNoContentType() throws DataValidationException {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("13");
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setLanguageLocationCode("123");
        contentUploadCsv.setContentName("Content");
        contentUploadCsv.setContentFile("NewFile");
        contentUploadCsv.setCardNumber("10");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setModifiedBy("Etasha");
        contentUploadCsv.setOwner("Etasha");
        contentUploadCsv.setCreator("Etasha");

        ContentUploadCsv contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);
        assertNotNull(contentUploadCsvRecordDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);
        throw new DataValidationException();

    }


    @Test
    public void testContentUploadNoContentFile() throws DataValidationException {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("13");
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setLanguageLocationCode("123");
        contentUploadCsv.setContentName("Content");
        contentUploadCsv.setContentType("PROMPT");
        contentUploadCsv.setCardNumber("10");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setModifiedBy("Etasha");
        contentUploadCsv.setOwner("Etasha");
        contentUploadCsv.setCreator("Etasha");

        ContentUploadCsv contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);
        assertNotNull(contentUploadCsvRecordDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);
        throw new DataValidationException();

    }

    @Test
    public void testContentUploadNoCardNumber() throws DataValidationException {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("13");
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setLanguageLocationCode("123");
        contentUploadCsv.setContentName("Content");
        contentUploadCsv.setContentType("PROMPT");
        contentUploadCsv.setContentFile("NewFile");
        contentUploadCsv.setContentDuration("120");
        contentUploadCsv.setModifiedBy("Etasha");
        contentUploadCsv.setOwner("Etasha");
        contentUploadCsv.setCreator("Etasha");

        ContentUploadCsv contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);
        assertNotNull(contentUploadCsvRecordDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);
        throw new DataValidationException();

    }

    @Test
    public void testContentUploadNoContentDuration() throws DataValidationException {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("13");
        contentUploadCsv.setCircleCode("CircleCode");
        contentUploadCsv.setLanguageLocationCode("123");
        contentUploadCsv.setContentName("Content");
        contentUploadCsv.setContentType("PROMPT");
        contentUploadCsv.setContentFile("NewFile");
        contentUploadCsv.setCardNumber("10");
        contentUploadCsv.setModifiedBy("Etasha");
        contentUploadCsv.setOwner("Etasha");
        contentUploadCsv.setCreator("Etasha");

        ContentUploadCsv contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);
        assertNotNull(contentUploadCsvRecordDataService);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        thrown.expect(DataValidationException.class);

        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(13);
        assertNull(contentUpload);
        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);
        throw new DataValidationException();

    }

}
