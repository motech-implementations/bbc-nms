package org.motechproject.nms.kilkari.osgi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
import org.motechproject.nms.kilkari.event.handler.ChildMctsCsvHandler;
import org.motechproject.nms.kilkari.repository.ChildMctsCsvDataService;
import org.motechproject.nms.kilkari.repository.MotherMctsCsvDataService;
import org.motechproject.nms.kilkari.service.ChildMctsCsvService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.nms.kilkari.service.LocationValidatorService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.repository.HealthBlockRecordsDataService;
import org.motechproject.nms.masterdata.repository.HealthFacilityRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaRecordsDataService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
/**
 * Verify that HelloWorldRecordService present, functional.
 */

public class LocationValidatorServiceUT {

    @Mock
    private StateRecordsDataService stateRecordsDataService;
    
    @Mock
    private DistrictRecordsDataService districtRecordsDataService;
    
    @Mock
    private TalukaRecordsDataService talukaRecordsDataService;
    
    @Mock
    private HealthBlockRecordsDataService healthBlockRecordsDataService;
    
    @Mock
    private HealthFacilityRecordsDataService healthFacilityRecordsDataService;
    
    @Mock
    protected SubscriberService subscriberService;
    
    @Mock
    protected MotherMctsCsvDataService motherMctsCsvDataService;
    
    @Mock
    protected MotherMctsCsvService motherMctsCsvService;
    
    @Mock
    protected ChildMctsCsvDataService childMctsCsvDataService;
    
    @Mock
    protected ChildMctsCsvService childMctsCsvService;

    @Mock
    protected SubscriptionService subscriptionService;

    @Mock
    protected LanguageLocationCodeService languageLocationCodeService;

    @Mock
    protected BulkUploadErrLogService bulkUploadErrLogService;

    @Mock
    protected ConfigurationService configurationService;

    @Mock
    protected LocationValidatorService locationValidatorService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testValidateCircleAndLLC() {
        
        System.out.println("Inside  callChildMctsCsvHandlerSuccessEvent");
        Map<String, Object> parameters = new HashMap<>();
        List<Long> uploadedIds = new ArrayList<Long>();
        
        uploadedIds.add(50l);
        System.out.println("uploadCsv().size()::::::::::::::::" +uploadedIds.size());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "ChildMctsCsvUT.csv");
        
        ChildMctsCsvHandler childMctsCsvHandler = new ChildMctsCsvHandler(childMctsCsvService, 
                subscriptionService, 
                subscriberService, 
                locationValidatorService, 
                languageLocationCodeService, 
                bulkUploadErrLogService, 
                configurationService);
        
        MotechEvent motechEvent = new MotechEvent("ChildMctsCsv.csv_success", parameters);
        childMctsCsvHandler.childMctsCsvSuccess(motechEvent);
        
        
    }
    
    protected ChildMctsCsv createChildMcts(ChildMctsCsv csv) {
        csv.setStateCode("1");
        csv.setDistrictCode("1");
        csv.setTalukaCode("1");
        csv.setHealthBlockCode("1");
        csv.setPhcCode("1");
        csv.setSubCentreCode("1");
        csv.setVillageCode("1");
        csv.setMotherName("motherName");
        csv.setBirthdate("2001-01-01 00:00:00");
        csv.setEntryType("Death");
        csv.setCreator("Deepak");
        csv.setOwner("Deepak");
        csv.setModifiedBy("Deepak");
        return csv;
    }
}
