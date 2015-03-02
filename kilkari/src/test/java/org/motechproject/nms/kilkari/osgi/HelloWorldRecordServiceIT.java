package org.motechproject.nms.kilkari.osgi;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.event.handler.MotherMctsCsvHandler;
import org.motechproject.nms.kilkari.repository.MotherMctsCsvDataService;
import org.motechproject.nms.kilkari.service.ConfigurationService;
import org.motechproject.nms.kilkari.service.LocationValidatorService;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
import org.motechproject.nms.kilkari.service.SubscriberService;
import org.motechproject.nms.kilkari.service.SubscriptionService;
import org.motechproject.nms.masterdata.service.LanguageLocationCodeService;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
/**
 * Verify that HelloWorldRecordService present, functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class HelloWorldRecordServiceIT extends CommonStructure {

    @Inject
    private SubscriberService subscriberService;
    
    @Inject
    private MotherMctsCsvDataService motherMctsCsvDataService;
    
    @Inject
    private MotherMctsCsvService motherMctsCsvService;

    @Inject
    private SubscriptionService subscriptionService;

    @Inject
    private LanguageLocationCodeService languageLocationCodeService;

    @Inject
    private BulkUploadErrLogService bulkUploadErrLogService;

    @Inject
    private ConfigurationService configurationService;

    @Inject
    private LocationValidatorService locationValidatorService;
    
    @Test
    public void createSubscriptionSubscriberTest() throws Exception {
        System.out.println("Mother Mcts");
        setUp();
        setUp(uploadedMotherMctsIds()); //create new record
        
        assertNotNull(subscriberService);
        System.out.println("Hello");
        assertNotNull(motherMctsCsvDataService);
    }
    
    private List<Long> uploadedMotherMctsIds() {
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = createMotherMcts();
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        return uploadedIds;
    }
    
    private void setUp(List<Long> uploadedIds){
        Map<String, Object> parameters = new HashMap<>();
        System.out.println("uploadCsv().size()::::::::::::::::" +uploadedIds.size());
        parameters.put("csv-import.created_ids", uploadedIds);
        parameters.put("csv-import.filename", "MotherMctsCsv.csv");
        
        MotherMctsCsvHandler motherMctsCsvHandler = new MotherMctsCsvHandler(motherMctsCsvService, 
                subscriptionService, 
                subscriberService, 
                locationValidatorService, 
                languageLocationCodeService, 
                bulkUploadErrLogService, 
                configurationService);
        
        MotechEvent motechEvent = new MotechEvent("MotherMctsCsv.csv_success", parameters);
        motherMctsCsvHandler.motherMctsCsvSuccess(motechEvent);
    }
    
    @Test
    public void createSameMsisdnDifferentMcts() throws Exception {
        System.out.println("Mother Mcts");
        setUp(createSameMsisdnDifferentMctsIds()); // Record_Already_Exist
        
        assertNotNull(subscriberService);
        System.out.println("Hello");
        assertNotNull(motherMctsCsvDataService);
    }
    
    private List<Long> createSameMsisdnDifferentMctsIds() {
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = createMotherMcts();
        csv.setIdNo("1234"); 
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        return uploadedIds;
    }
    
    @Test
    public void createSameMsisdnSameMcts() throws Exception {
        System.out.println("Mother Mcts");
        setUp(createSameMsisdnSameMctsIds()); // Record update when matching Msisdn and Mctsid
        
        assertNotNull(subscriberService);
        System.out.println("Hello");
        assertNotNull(motherMctsCsvDataService);
    }
    
    private List<Long> createSameMsisdnSameMctsIds() {
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = createMotherMcts();
        csv.setName("testing");
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        return uploadedIds;
    }
    
    @Test
    public void createDifferentMsisdnSameMcts() throws Exception {
        System.out.println("Mother Mcts");
        setUp(createDifferentMsisdnSameMctsIds()); // Record update when different Msisdn and matching Mctsid
        
        assertNotNull(subscriberService);
        System.out.println("Hello");
        assertNotNull(motherMctsCsvDataService);
    }
    
    private List<Long> createDifferentMsisdnSameMctsIds() {
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = createMotherMcts();
        csv.setWhomPhoneNo("1234567890");
        csv.setName("testing234");
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        return uploadedIds;
    }
    
    @Test
    public void createDifferentMsisdnDifferentMcts() throws Exception {
        System.out.println("Mother Mcts");
        setUp(createDifferentMsisdnDifferentMctsIds()); // create New Record
        
        assertNotNull(subscriberService);
        System.out.println("Hello");
        assertNotNull(motherMctsCsvDataService);
    }
    
    private List<Long> createDifferentMsisdnDifferentMctsIds() {
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = createMotherMcts();
        csv.setWhomPhoneNo("9876543576");
        csv.setIdNo("9876543576");
        csv.setName("testing234");
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        return uploadedIds;
    }
    
    @Test
    public void createDeleteOperation() throws Exception {
        System.out.println("Mother Mcts");
        setUp(createDeleteOperationIds()); // Operation Delete, Deactivate Subscription
        
        assertNotNull(subscriberService);
        System.out.println("Hello");
        assertNotNull(motherMctsCsvDataService);
    }
    
    private List<Long> createDeleteOperationIds() {
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = createMotherMcts();
        csv.setWhomPhoneNo("9876543576");
        csv.setIdNo("9876543576");
        csv.setName("testing234");
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        return uploadedIds;
    }
    
    
    
    private MotherMctsCsv createMotherMcts() {
        MotherMctsCsv csv = new MotherMctsCsv();
        csv.setStateCode("1");
        csv.setDistrictCode("1");
        csv.setTalukaCode("1");
        csv.setHealthBlockCode("1");
        csv.setPhcCode("1");
        csv.setSubCentreCode("1");
        csv.setVillageCode("1");
        csv.setIdNo("123");
        csv.setName("test");
        csv.setWhomPhoneNo("9876543456");
        csv.setLmpDate("10-12-2014");
        csv.setAbortion("Abortion");
        csv.setOutcomeNos("0");
        csv.setAge("30");
        csv.setEntryType("Birth");
        csv.setAadharNo("123456789876");
        return csv;
    }
    
    
}
