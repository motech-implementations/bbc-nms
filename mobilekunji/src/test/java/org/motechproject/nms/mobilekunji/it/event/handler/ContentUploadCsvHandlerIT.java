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

        ContentUploadCsv contentUploadCsv1 = new ContentUploadCsv();
        contentUploadCsv1.setIndex(1L);
        contentUploadCsv1.setContentId("12");
        contentUploadCsv1.setCircleCode("CircleCode");
        contentUploadCsv1.setLanguageLocationCode("123");
        contentUploadCsv1.setContentName("Content");
        contentUploadCsv1.setContentType("PROMPT");
        contentUploadCsv1.setContentFile("NewFile");
        contentUploadCsv1.setCardNumber("10");
        contentUploadCsv1.setContentDuration("120");
        contentUploadCsv1.setModifiedBy("Etasha");
        contentUploadCsv1.setOwner("Etasha");
        contentUploadCsv1.setCreator("Etasha");
        ContentUploadCsv contentUploadCsv2 = new ContentUploadCsv();
        contentUploadCsv2.setIndex(2L);
        contentUploadCsv2.setContentId("22");
        contentUploadCsv2.setCircleCode("CircleCode");
        contentUploadCsv2.setLanguageLocationCode("123");
        contentUploadCsv2.setContentName("Content");
        contentUploadCsv2.setContentType("CONTENT");
        contentUploadCsv2.setContentFile("NewFile");
        contentUploadCsv2.setCardNumber("11");
        contentUploadCsv2.setContentDuration("120");
        contentUploadCsv2.setModifiedBy("Etasha");
        contentUploadCsv2.setOwner("Etasha");
        contentUploadCsv2.setCreator("Etasha");

        ContentUploadCsv contentUploadCsv3 = new ContentUploadCsv();

        contentUploadCsv3.setIndex(3L);
        contentUploadCsv3.setContentId("33");
        contentUploadCsv3.setCircleCode("CircleCode");
        contentUploadCsv3.setLanguageLocationCode("123");
        contentUploadCsv3.setContentName("Content");
        contentUploadCsv3.setContentType("CONTENTS");
        contentUploadCsv3.setContentFile("NewFile");
        contentUploadCsv3.setCardNumber("12");
        contentUploadCsv3.setContentDuration("120");
        contentUploadCsv3.setModifiedBy("Etasha");
        contentUploadCsv3.setOwner("Etasha");
        contentUploadCsv3.setCreator("Etasha");


        ContentUploadCsv contentUploadCsv4 = new ContentUploadCsv();
        contentUploadCsv4.setIndex(4L);
        contentUploadCsv4.setContentId("12");
        contentUploadCsv4.setCircleCode("CircleCode");
        contentUploadCsv4.setLanguageLocationCode("124");
        contentUploadCsv4.setContentName("Content");
        contentUploadCsv4.setContentType("PROMPT");
        contentUploadCsv4.setContentFile("NewFile");
        contentUploadCsv4.setCardNumber("10");
        contentUploadCsv4.setContentDuration("120");
        contentUploadCsv4.setModifiedBy("Etasha");
        contentUploadCsv4.setOwner("Etasha");
        contentUploadCsv4.setCreator("Etasha");

        ContentUploadCsv contentUploadCsvDb1 = contentUploadCsvRecordDataService.create(contentUploadCsv1);
        ContentUploadCsv contentUploadCsvDb2 = contentUploadCsvRecordDataService.create(contentUploadCsv2);
        ContentUploadCsv contentUploadCsvDb3 = contentUploadCsvRecordDataService.create(contentUploadCsv3);
        ContentUploadCsv contentUploadCsvDb4 = contentUploadCsvRecordDataService.create(contentUploadCsv4);
        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb1.getId());
        uploadedIds.add(contentUploadCsvDb2.getId());
        uploadedIds.add(contentUploadCsvDb3.getId());
        uploadedIds.add(contentUploadCsvDb4.getId());

        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ContentUpload.csv");

        MotechEvent motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(12);

        assertNotNull(contentUpload);


        assertTrue(12 == contentUpload.getContentId());
        assertEquals("CircleCode", contentUpload.getCircleCode());
        assertTrue(124 == contentUpload.getLanguageLocationCode());
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

/*

    @Ignore
    public void testContentUploadUpdation() {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("14");
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
        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(14);

        assertNotNull(contentUpload);

        //Updation
        contentUploadCsv.setIndex(2L);
        contentUploadCsv.setContentId("15");
        contentUploadCsv.setCircleCode("CircleCodeNew");
        contentUploadCsv.setLanguageLocationCode("1234");
        contentUploadCsv.setContentName("ContentNew");
        contentUploadCsv.setContentType("CONTENT");
        contentUploadCsv.setContentFile("NewFile2");
        contentUploadCsv.setCardNumber("11");
        contentUploadCsv.setContentDuration("240");
        contentUploadCsv.setModifiedBy("Etasha");
        contentUploadCsv.setOwner("Etasha");
        contentUploadCsv.setCreator("Etasha");

        contentUploadCsvDb = contentUploadCsvRecordDataService.update(contentUploadCsv);


        Map<String, Object> parametersNew = new HashMap<>();
        List<Long> uploadedIdsNew = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parametersNew.put("csv-import.created_ids", uploadedIdsNew);
        parametersNew.put("csv-import.filename", "ContentUpload.csv");

        motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        contentUpload = contentUploadRecordDataService.findRecordByContentId(15);


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


    @Ignore
    public void testContentUploadDeletionWhenPresentInDb() {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("16");
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
        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(16);

        assertNotNull(contentUpload);



        //Deletion

        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("16");
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
        contentUploadCsv.setOperation("DEL");

        contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);

        contentUploadCsvDb = contentUploadCsvRecordDataService.delete(contentUploadCsvDb);


        Map<String, Object> parametersNew = new HashMap<>();
        List<Long> uploadedIdsNew = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parametersNew.put("csv-import.created_ids", uploadedIdsNew);
        parametersNew.put("csv-import.filename", "ContentUpload.csv");

        motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        contentUpload = contentUploadRecordDataService.findRecordByContentId(15);

        assertNull(contentUpload);

        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);
    }*/

/*

    @Ignore
    public void testContentUploadDeletionWhenPresentInDb() {

        ContentUploadCsv contentUploadCsv = new ContentUploadCsv();
        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("16");
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
        ContentUpload contentUpload = contentUploadRecordDataService.findRecordByContentId(16);

        assertNotNull(contentUpload);



        //Deletion

        contentUploadCsv.setIndex(1L);
        contentUploadCsv.setContentId("17");
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

        contentUploadCsvDb = contentUploadCsvRecordDataService.create(contentUploadCsv);

        contentUploadCsvDb = contentUploadCsvRecordDataService.delete(contentUploadCsvDb);


        Map<String, Object> parametersNew = new HashMap<>();
        List<Long> uploadedIdsNew = new ArrayList<Long>();

        uploadedIds.add(contentUploadCsvDb.getId());
        parametersNew.put("csv-import.created_ids", uploadedIdsNew);
        parametersNew.put("csv-import.filename", "ContentUpload.csv");

        motechEvent = new MotechEvent("ContentUploadCsv.csv_success", parameters);
        contentUploadCsvHandler.mobileKunjiContentUploadSuccess(motechEvent);
        contentUpload = contentUploadRecordDataService.findRecordByContentId(15);

        assertNull(contentUpload);

        List<ContentUploadCsv> listContentUploadCsv = contentUploadCsvRecordDataService.retrieveAll();
        assertTrue(listContentUploadCsv.size() == 0);
    }

*/
}
