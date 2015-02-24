package org.motechproject.nms.masterdata.it;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.masterdata.constants.LocationConstants;
import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.event.handler.LanguageLocationCodeCsvHandler;
import org.motechproject.nms.masterdata.repository.CircleDataService;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeCsvDataService;
import org.motechproject.nms.masterdata.repository.LanguageLocationCodeDataService;
import org.motechproject.nms.masterdata.service.*;
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

/**
 * This class is used to test(IT) the operations of Language Location Code Csv
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class LanguageLocationCodeCsvHandlerIT extends BasePaxIT {

    @Inject
    private LanguageLocationCodeDataService llcDataService;

    @Inject
    private LanguageLocationCodeService languageLocationCodeService;

    @Inject
    private LanguageLocationCodeServiceCsv languageLocationCodeServiceCsv;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    private CircleService circleService;

    @Inject
    private LocationService locationService;

    @Inject
    private LanguageLocationCodeCsvDataService llcCsvDataService;

    @Inject
    private DistrictService districtService;

    @Inject
    private StateService stateService;

    @Inject
    private CircleDataService circleDataService;

    @Before
    public void setUp() {
        llcDataService.deleteAll();
        districtService.deleteAll();
        stateService.deleteAll();
        circleDataService.deleteAll();
    }

    List<Long> createdIds = new ArrayList<Long>();

    @Test
    public void shouldCreateLanguageLocationCodeRecordsAfterCsvUpload() throws Exception {
        LanguageLocationCodeCsvHandler llcCsvHandler = new LanguageLocationCodeCsvHandler(languageLocationCodeService,
                languageLocationCodeServiceCsv, bulkUploadErrLogService, circleService, locationService);
        preSetUp();

        llcCsvHandler.languageLocationCodeCsvSuccess(createMotechEvent(createdIds));

        //Do Assertions
        LanguageLocationCodeCsv csvRecord = languageLocationCodeServiceCsv.getRecord(createdIds.get(0));
        Assert.assertNull(csvRecord);

        LanguageLocationCode record = languageLocationCodeService.getRecordByLocationCode(1L, 1L);
        Assert.assertEquals(record.getCircleCode(), "testCode");
        Assert.assertTrue(record.getDistrictCode().equals(1L));
        Assert.assertTrue(record.getStateCode().equals(1L));
        Assert.assertTrue(record.getLanguageKK().equals("LanguageKK"));
        Assert.assertTrue(record.getLanguageMA().equals("LanguageMA"));
        Assert.assertTrue(record.getLanguageMK().equals("LanguageMK"));
        Assert.assertTrue(record.getLanguageLocationCode().equals(123));
    }

    @Test
    public void shouldUpdateLanguageLocationCodeRecordsAfterCsvUpload() throws Exception {
        LanguageLocationCodeCsvHandler llcCsvHandler = new LanguageLocationCodeCsvHandler(languageLocationCodeService,
                languageLocationCodeServiceCsv, bulkUploadErrLogService,
                circleService, locationService);

        preSetUp();

        //create LanguageLocationCodeCsv record with circleCode "testCode",
        // districtCode "1" and stateCode "1"
        LanguageLocationCodeCsv csvRecord = new LanguageLocationCodeCsv();
        csvRecord.setCircleCode("testCode");
        csvRecord.setDistrictCode("1");
        csvRecord.setStateCode("1");
        csvRecord.setIsDefaultLanguageLocationCode("true");
        csvRecord.setLanguageKK("LanguageKKChanged");
        csvRecord.setLanguageMA("LanguageMAChanged");
        csvRecord.setLanguageMK("LanguageMKChanged");
        csvRecord.setLanguageLocationCode("321");
        LanguageLocationCodeCsv dbCsv = llcCsvDataService.create(csvRecord);
        createdIds.add(dbCsv.getId());

        llcCsvHandler.languageLocationCodeCsvSuccess(createMotechEvent(createdIds));

        //Do Assertions
        csvRecord = languageLocationCodeServiceCsv.getRecord(createdIds.get(0));
        Assert.assertNull(csvRecord);

        LanguageLocationCode record = languageLocationCodeService.getRecordByLocationCode(1L, 1L);
        Assert.assertEquals(record.getCircleCode(), "testCode");
        Assert.assertTrue(record.getDistrictCode().equals(1L));
        Assert.assertTrue(record.getStateCode().equals(1L));
        Assert.assertTrue(record.getLanguageKK().equals("LanguageKKChanged"));
        Assert.assertTrue(record.getLanguageMA().equals("LanguageMAChanged"));
        Assert.assertTrue(record.getLanguageMK().equals("LanguageMKChanged"));
        Assert.assertTrue(record.getLanguageLocationCode().equals(321));
    }

    @Test
    public void shouldWriteErrorLogIfCsvRecordIsNotFound() throws Exception {
        LanguageLocationCodeCsvHandler llcCsvHandler = new LanguageLocationCodeCsvHandler(languageLocationCodeService,
                languageLocationCodeServiceCsv, bulkUploadErrLogService, circleService, locationService);
        createdIds.add(1L);

        llcCsvHandler.languageLocationCodeCsvSuccess(createMotechEvent(createdIds));
    }

    @Test
    public void shouldRaiseDataValidationException() throws Exception {
        LanguageLocationCodeCsvHandler llcCsvHandler = new LanguageLocationCodeCsvHandler(languageLocationCodeService,
                languageLocationCodeServiceCsv, bulkUploadErrLogService, circleService, locationService);
        //create circle with code "testCode"
        Circle circle = new Circle();
        circle.setName("name");
        circle.setCode("testCode");
        circle.setDefaultLanguageLocationCode(123);
        circleService.create(circle);

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

        //create LanguageLocationCodeCsv record with circleCode "testCode",
        // districtCode "1" and stateCode "1"
        LanguageLocationCodeCsv csvRecord = new LanguageLocationCodeCsv();
        csvRecord.setCircleCode("testCode");
        csvRecord.setDistrictCode("1");
        csvRecord.setStateCode("@");
        csvRecord.setIsDefaultLanguageLocationCode("Y");
        csvRecord.setLanguageKK("LanguageKK");
        csvRecord.setLanguageMA("LanguageMA");
        csvRecord.setLanguageMK("LanguageMK");
        csvRecord.setLanguageLocationCode("123");
        LanguageLocationCodeCsv dbCsv = llcCsvDataService.create(csvRecord);
        createdIds.add(dbCsv.getId());

        llcCsvHandler.languageLocationCodeCsvSuccess(createMotechEvent(createdIds));
    }

    public MotechEvent createMotechEvent(List<Long> ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("csv-import.created_ids", ids);
        params.put("csv-import.filename", "languageLocationCode");
        return new MotechEvent(LocationConstants.LANGUAGE_LOCATION_CODE_CSV_SUCCESS, params);
    }

    public void preSetUp() {
        //create circle with code "testCode"
        Circle circle = new Circle();
        circle.setName("testCircle");
        circle.setCode("testCode");
        circle.setDefaultLanguageLocationCode(123);
        circleService.create(circle);

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

        //create LanguageLocationCodeCsv record with circleCode "testCode",
        // districtCode "1" and stateCode "1"
        LanguageLocationCodeCsv csvRecord = new LanguageLocationCodeCsv();
        csvRecord.setCircleCode("testCode");
        csvRecord.setDistrictCode("1");
        csvRecord.setStateCode("1");
        csvRecord.setIsDefaultLanguageLocationCode("true");
        csvRecord.setLanguageKK("LanguageKK");
        csvRecord.setLanguageMA("LanguageMA");
        csvRecord.setLanguageMK("LanguageMK");
        csvRecord.setLanguageLocationCode("123");
        LanguageLocationCodeCsv dbCsv = llcCsvDataService.create(csvRecord);
        createdIds.add(dbCsv.getId());
    }

    @After
    public void tearDown() {
        for (Long id : createdIds) {
            LanguageLocationCode ll = llcDataService.findById(id);

            if (ll != null) {
                Circle circle = circleService.getRecordByCode(ll.getCircleCode());
                State state = locationService.getStateByCode(ll.getStateCode());
                District district = null;
                if (state != null) {
                    district = locationService.getDistrictByCode(state.getStateCode(), ll.getDistrictCode());
                    stateService.delete(state);
                }

                llcDataService.delete(ll);
                if (circle != null) {
                    circleService.delete(circle);
                }
                if (district != null) {
                    districtService.delete(district);
                }
            }
        }
        createdIds.clear();
    }
}
