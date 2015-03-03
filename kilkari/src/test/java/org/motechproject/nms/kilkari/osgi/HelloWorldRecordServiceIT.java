package org.motechproject.nms.kilkari.osgi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
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
        System.out.println("Inside createSubscriptionSubscriberTest");
        setUp();
        
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = new MotherMctsCsv();
        csv = createMotherMcts(csv);
        csv.setWhomPhoneNo("1");
        csv.setIdNo("1");
        
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds); //create new record
        
        Subscription dbSubscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        Subscriber dbSubscriber = dbSubscription.getSubscriber();
        assertNotNull(dbSubscription);
        assertNotNull(dbSubscriber);
        assertTrue(dbSubscription.getChannel().equals(Channel.MCTS));
        assertTrue(dbSubscriber.getName().equals(csv.getName()));
        assertTrue(dbSubscriber.getState().getStateCode().toString().equals(csv.getStateCode()));
    } 
    
    @Test
    public void createSameMsisdnDifferentMcts() throws Exception {
        System.out.println("Inside createSameMsisdnDifferentMcts");
        setUp();
        
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = new MotherMctsCsv();
        csv = createMotherMcts(csv);
        csv.setWhomPhoneNo("2");
        csv.setIdNo("2");
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        
        MotherMctsCsv csv1 = new MotherMctsCsv();
        csv1 = createMotherMcts(csv1);
        csv1.setWhomPhoneNo("2");
        csv1.setIdNo("3");
        MotherMctsCsv dbCsv1 = motherMctsCsvDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds); // Record_Already Exist
        
        Subscription dbSubscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        assertNotNull(dbSubscription);
        dbSubscription = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode()));
        assertNull(dbSubscription);
    }
    
    @Test
    public void createSameMsisdnSameMcts() throws Exception {
        System.out.println("Inside createSameMsisdnSameMcts");
        setUp();
        
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = new MotherMctsCsv();
        csv = createMotherMcts(csv);
        csv.setWhomPhoneNo("4");
        csv.setIdNo("4");
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        MotherMctsCsv csv1 = new MotherMctsCsv();
        csv1 = createMotherMcts(csv1);
        csv1.setWhomPhoneNo("4");
        csv1.setIdNo("4");
        csv1.setName("testing");
        csv1.setLmpDate("2015-01-20 08:30:30");
        MotherMctsCsv dbCsv1 = motherMctsCsvDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds); // Record update when matching Msisdn and Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode()));
        
        assertNotNull(subscription);
        assertNotNull(updateSubs);
        assertNotNull(subscription.getSubscriber());
        assertNotNull(updateSubs.getSubscriber());
        assertFalse(subscription.getSubscriber().getName().equals(updateSubs.getSubscriber().getName()));
        assertFalse(subscription.getSubscriber().getLmp().equals(updateSubs.getSubscriber().getLmp()));
    }
    
    @Test
    public void createDifferentMsisdnSameMcts() throws Exception {
        System.out.println("Inside createDifferentMsisdnSameMcts");
        setUp();
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = new MotherMctsCsv();
        csv = createMotherMcts(csv);
        csv.setWhomPhoneNo("5");
        csv.setIdNo("5");
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        MotherMctsCsv csv1 = new MotherMctsCsv();
        csv1 = createMotherMcts(csv1);
        csv1.setWhomPhoneNo("6");
        csv1.setIdNo("5");
        csv1.setName("testDifferentName");
        csv1.setLmpDate("2015-01-22 08:30:30");
        MotherMctsCsv dbCsv1 = motherMctsCsvDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds); // Record update when different Msisdn and matching Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode()));

        
        assertNotNull(subscription);
        assertNotNull(updateSubs);
        assertNotNull(subscription.getSubscriber());
        assertNotNull(updateSubs.getSubscriber());
        assertTrue(!subscription.getSubscriber().getName().equals(updateSubs.getSubscriber().getName()));
        assertTrue(!subscription.getSubscriber().getLmp().equals(updateSubs.getSubscriber().getLmp()));
    }
    
    @Test
    public void createDeleteOperation() throws Exception {
        System.out.println("Inside  createDeleteOperation");
        setUp();
        
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = new MotherMctsCsv();
        csv = createMotherMcts(csv);
        csv.setWhomPhoneNo("12");
        csv.setIdNo("7");
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        MotherMctsCsv csv1 = new MotherMctsCsv();
        csv1 = createMotherMcts(csv1);
        csv1.setWhomPhoneNo("13");
        csv1.setIdNo("7");
        csv1.setOperation("Delete");
        MotherMctsCsv dbCsv1 = motherMctsCsvDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds); // Record update when different Msisdn and matching Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode())); //Operation Delete, Deactivate Subscription
        
        assertFalse(subscription.getStatus().equals(updateSubs.getStatus()));
        assertTrue(subscription.getSubscriber().getName().equals(updateSubs.getSubscriber().getName()));
    }
    
    private void callMotherMctsCsvHandlerSuccessEvent(List<Long> uploadedIds){
        System.out.println("Inside  callMotherMctsCsvHandlerSuccessEvent");
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
    
    private MotherMctsCsv createMotherMcts(MotherMctsCsv csv) {
        csv.setStateCode("1");
        csv.setDistrictCode("1");
        csv.setTalukaCode("1");
        csv.setHealthBlockCode("1");
        csv.setPhcCode("1");
        csv.setSubCentreCode("1");
        csv.setVillageCode("1");
        csv.setName("test");
        csv.setLmpDate("10-12-2014 08:00:00");
        csv.setAbortion("Abortion");
        csv.setOutcomeNos("0");
        csv.setAge("30");
        csv.setEntryType("Birth");
        csv.setAadharNo("123456789876");
        return csv;
    }
    
    
}
