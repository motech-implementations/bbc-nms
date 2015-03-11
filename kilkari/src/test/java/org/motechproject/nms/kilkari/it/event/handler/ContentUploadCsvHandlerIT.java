package org.motechproject.nms.kilkari.it.event.handler;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.kilkari.domain.ContentUpload;
import org.motechproject.nms.kilkari.domain.ContentUploadCsv;
import org.motechproject.nms.kilkari.event.handler.ContentUploadCsvHandler;
import org.motechproject.nms.kilkari.repository.ContentUploadCsvDataService;
import org.motechproject.nms.kilkari.repository.ContentUploadDataService;
import org.motechproject.nms.kilkari.service.ContentUploadCsvService;
import org.motechproject.nms.kilkari.service.ContentUploadService;
import org.motechproject.nms.masterdata.constants.MasterDataConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.*;
import org.motechproject.nms.masterdata.service.CircleService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
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

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ContentUploadCsvHandlerIT extends BasePaxIT {

    @Inject
    private CircleService circleService;

    @Inject
    private LanguageLocationCodeService languageLocationCodeService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    private ContentUploadService contentUploadService;

    @Inject
    private ContentUploadCsvService contentUploadCsvService;

    @Inject
    private ContentUploadCsvDataService contentUploadCsvDataService;

    @Inject
    private ContentUploadDataService contentUploadDataService;

    @Inject
    private CircleDataService circleDataService;

    @Inject
    private StateRecordsDataService stateService;

    @Inject
    private DistrictRecordsDataService districtService;

    @Inject
    private LanguageLocationCodeDataService llcDataService;

    List<Long> createdIds = new ArrayList<Long>();

    @Before
    public void setUp() {
        llcDataService.deleteAll();
        circleDataService.deleteAll();
        contentUploadDataService.deleteAll();
    }

    @Test
    public void shouldCreateContentUploadRecordsAfterCsvUpload() throws Exception {
        ContentUploadCsvHandler csvHandler = new ContentUploadCsvHandler(bulkUploadErrLogService, contentUploadService,
                contentUploadCsvService, circleService, languageLocationCodeService);
        preSetup();


        csvHandler.contentUploadCsvSuccess(createMotechEvent(createdIds));

        Assert.assertNull(contentUploadCsvService.getRecord(createdIds.get(0)));

        ContentUpload record = contentUploadService.getRecordByContentId(1L);
        Assert.assertTrue(record.getContentDuration() == 10);
        Assert.assertEquals(record.getContentType().name(), "PROMPT");
        Assert.assertEquals(record.getContentName(), "contentName");
        Assert.assertTrue(record.getLanguageLocationCode().toString().equals("123"));
        Assert.assertEquals(record.getCircleCode(), "circleCode");
        Assert.assertEquals(record.getContentFile(), "contentFile");
    }

    @Test
    public void shouldUpdateContentUploadRecordsAfterCsvUpload() throws Exception {
        ContentUploadCsvHandler csvHandler = new ContentUploadCsvHandler(bulkUploadErrLogService, contentUploadService,
                contentUploadCsvService, circleService, languageLocationCodeService);

        preSetup();

        ContentUploadCsv csv = new ContentUploadCsv();
        csv.setLanguageLocationCode("123");
        csv.setContentType("contentTypeChanged");
        csv.setContentFile("contentFileChanged");
        csv.setCircleCode("circleCode");
        csv.setContentName("contentNameChanged");
        csv.setContentDuration("100");
        csv.setContentId("1");
        ContentUploadCsv dbCsv = contentUploadCsvDataService.create(csv);
        createdIds.add(dbCsv.getId());

        csvHandler.contentUploadCsvSuccess(createMotechEvent(createdIds));
        Assert.assertNull(contentUploadCsvService.getRecord(createdIds.get(0)));
        Assert.assertNull(contentUploadCsvService.getRecord(createdIds.get(1)));

        ContentUpload record = contentUploadService.getRecordByContentId(1L);
        Assert.assertTrue(record.getContentDuration() == 100);
        Assert.assertEquals(record.getContentType().name(), "PROMPT");
        Assert.assertEquals(record.getContentName(), "contentNameChanged");
        Assert.assertTrue(record.getLanguageLocationCode().toString().equals("123"));
        Assert.assertEquals(record.getCircleCode(), "circleCode");
        Assert.assertEquals(record.getContentFile(), "contentFileChanged");
    }

    @Test
    public void shouldWriteErrorLogIfCsvRecordIsNotFound() throws Exception {
        ContentUploadCsvHandler csvHandler = new ContentUploadCsvHandler(bulkUploadErrLogService, contentUploadService,
                contentUploadCsvService, circleService, languageLocationCodeService);

        createdIds.add(1L);
        csvHandler.contentUploadCsvSuccess(createMotechEvent(createdIds));
    }

    @Test
    public void shouldRaiseDataValidationException() throws Exception {
        ContentUploadCsvHandler csvHandler = new ContentUploadCsvHandler(bulkUploadErrLogService, contentUploadService,
                contentUploadCsvService, circleService, languageLocationCodeService);
        Circle circle = new Circle();
        circle.setName("MotechEventCreateTest");
        circle.setCode("circleCode");
        circleDataService.create(circle);

        //create State with statecode "1"
        State state = new State();
        state.setName("testState");
        state.setStateCode(1L);
        stateService.create(state);

        //create district with districtCode "1" and stateCode "1"
        District district = new District();
        district.setStateCode(1L);
        district.setName("testDistrict");
        district.setDistrictCode(1L);
        district.setStateCode(1L);
        districtService.create(district);

        LanguageLocationCode llc = new LanguageLocationCode();
        llc.setCircleCode("circleCode");
        llc.setDistrictCode(1L);
        llc.setStateCode(1L);
        llc.setLanguageLocationCode(123);
        llc.setLanguageKK("LanguageKK");
        llc.setLanguageMA("LanguageMA");
        llc.setLanguageMK("LanguageMK");
        llc.setCircle(circle);
        llc.setDistrict(district);
        llc.setState(state);
        languageLocationCodeService.create(llc);

        ContentUploadCsv contentCsv = new ContentUploadCsv();
        contentCsv.setLanguageLocationCode("123@@@@");
        contentCsv.setContentType("contentType");
        contentCsv.setContentFile("contentFile");
        contentCsv.setCircleCode("circleCode");
        contentCsv.setContentName("contentName");
        contentCsv.setContentDuration("10");
        contentCsv.setContentId("1");
        ContentUploadCsv dbCsv = contentUploadCsvDataService.create(contentCsv);
        createdIds.add(dbCsv.getId());


        csvHandler.contentUploadCsvSuccess(createMotechEvent(createdIds));
    }

    public MotechEvent createMotechEvent(List<Long> ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("csv-import.created_ids", ids);
        params.put("csv-import.filename", "contentUpload");
        return new MotechEvent(MasterDataConstants.CIRCLE_CSV_SUCCESS, params);
    }

    public void preSetup() {
        Circle circle = new Circle();
        circle.setName("MotechEventCreateTest");
        circle.setCode("circleCode");
        circleDataService.create(circle);

        //create State with statecode "1"
        State state = new State();
        state.setName("testState");
        state.setStateCode(1L);
        stateService.create(state);

        //create district with districtCode "1" and stateCode "1"
        District district = new District();
        district.setStateCode(1L);
        district.setName("testDistrict");
        district.setDistrictCode(1L);
        district.setStateCode(1L);
        districtService.create(district);

        LanguageLocationCode llc = new LanguageLocationCode();
        llc.setCircleCode("circleCode");
        llc.setDistrictCode(1L);
        llc.setStateCode(1L);
        llc.setLanguageLocationCode(123);
        llc.setLanguageKK("LanguageKK");
        llc.setLanguageMA("LanguageMA");
        llc.setLanguageMK("LanguageMK");
        llc.setCircle(circle);
        llc.setDistrict(district);
        llc.setState(state);
        languageLocationCodeService.create(llc);

        ContentUploadCsv contentCsv = new ContentUploadCsv();
        contentCsv.setLanguageLocationCode("123");
        contentCsv.setContentType("contentType");
        contentCsv.setContentFile("contentFile");
        contentCsv.setCircleCode("circleCode");
        contentCsv.setContentName("contentName");
        contentCsv.setContentDuration("10");
        contentCsv.setContentId("1");
        ContentUploadCsv dbCsv = contentUploadCsvDataService.create(contentCsv);
        createdIds.add(dbCsv.getId());
    }

    @After
    public void tearDown() {
        contentUploadDataService.deleteAll();
        llcDataService.deleteAll();
        districtService.deleteAll();
        stateService.deleteAll();
        circleDataService.deleteAll();
    }
}