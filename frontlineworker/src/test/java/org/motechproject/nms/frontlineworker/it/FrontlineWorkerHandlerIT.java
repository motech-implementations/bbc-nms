package org.motechproject.nms.frontlineworker.it;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;
import org.motechproject.nms.frontlineworker.event.handler.FlwUploadHandler;
import org.motechproject.nms.frontlineworker.repository.FlwCsvRecordsDataService;
import org.motechproject.nms.frontlineworker.repository.FlwRecordDataService;
import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.masterdata.service.LocationService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

/**
 * Created by abhishek on 27/2/15.
 */

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class FrontlineWorkerHandlerIT extends BasePaxIT {

    @Inject
    private FlwRecordDataService flwRecordDataService;

    @Inject
    private FlwCsvRecordsDataService flwCsvRecordsDataService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    private LocationService locationService;

    @Inject
    private LanguageLocationCodeService languageLocationCodeService;

    @Inject
    private StateRecordsDataService stateRecordsDataService;

    private FlwUploadHandler flwUploadHandler;

    private State stateData;

    private District districtData;

    private Taluka talukaData;

    private Village villageData;

    private HealthBlock healthBlockData;

    private HealthFacility healthFacilityData;

    private HealthSubFacility healthSubFacilityData;


    @Before
    public void testFrontLineWorkerServicepresent() throws Exception {

        System.out.println("Front Line Worker Setup start");

        flwUploadHandler = new FlwUploadHandler(flwRecordDataService,
                flwCsvRecordsDataService,
                bulkUploadErrLogService,
                locationService,
                languageLocationCodeService);

        assertNotNull(flwRecordDataService);
        assertNotNull(flwCsvRecordsDataService);
        assertNotNull(bulkUploadErrLogService);
        assertNotNull(locationService);
        assertNotNull(languageLocationCodeService);

        State state = new State();
        state.setStateCode(12L);

        District district = new District();
        district.setStateCode(12L);
        district.setDistrictCode(123L);
        Set<District> districtSet = new HashSet<District>();
        districtSet.add(district);

        state = stateRecordsDataService.create(state);

        stateData = stateRecordsDataService.findRecordByStateCode(district.getStateCode());
        stateData.getDistrict().add(district);
        stateRecordsDataService.update(stateData);

        System.out.println("Front Line Worker Setup end");
    }


    @Test
    public void testFrontLineWorkerValidData() throws Exception {

        System.out.println("testFrontLineWorkerValidData start");
        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv("", "1", "12", "9990545494", "etasha", "USHA",
                "123", "", "", "", "", "123", "true", "1234", "add", "true");

        FrontLineWorkerCsv frontLineWorkerCsvdb = flwCsvRecordsDataService.create(frontLineWorkerCsv);

        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();

        uploadedIds.add(frontLineWorkerCsvdb.getId());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "FrontLineWorker.csv");


/*        FrontLineWorkerCsv frontLineWorkerCsv = new FrontLineWorkerCsv("1234", "1", "12", "9990545494", "etasha", "USHA",
                "123", "12345", "123456", "1234567", "12345678", "123", "true", "1234", "add", "true");
    */
/*        FrontLineWorkerCsv = new FrontLineWorkerCsv("1villagecode", "1flwid", "12statecode", "9990545494", "etasha", "USHA", "123ditrict",
                "1234taluka", "1hb", "1phc", "1ssc", "123asha", "isvalidated true", "1234adhar", "add", "isvalid true");*/


        MotechEvent motechEvent = new MotechEvent("FrontLineWorkerCsv.csv_success", parameters);
        flwUploadHandler.flwDataHandlerSuccess(motechEvent);

        System.out.println("testFrontLineWorkerValidData end");
    }

}

