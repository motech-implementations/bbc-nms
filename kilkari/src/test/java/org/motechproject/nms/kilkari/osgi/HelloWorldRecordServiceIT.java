package org.motechproject.nms.kilkari.osgi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Verify that HelloWorldRecordService present, functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class HelloWorldRecordServiceIT extends CommonStructure {

    private static Logger logger = LoggerFactory.getLogger(HelloWorldRecordServiceIT.class);
    @Test
    public void shouldCreateSubscriptionSubscriberTest() throws Exception {
        logger.info("Inside createSubscriptionSubscriberTest");
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
    public void shouldUpdateBasedSameMsisdnDifferentMcts() throws Exception {
        logger.info("Inside createSameMsisdnDifferentMcts");
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
    public void shouldUpdateBasedSameMsisdnSameMcts() throws Exception {
        logger.info("Inside createSameMsisdnSameMcts");
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
        csv1.setLmpDate("2015-01-20 08:08:08");
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
    public void shouldUpdateBasedChangedLmpDate() throws Exception {
        logger.info("Inside createSameMsisdnSameMcts");
        setUp();
        
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = new MotherMctsCsv();
        csv = createMotherMcts(csv);
        csv.setWhomPhoneNo("15");
        csv.setIdNo("15");
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        MotherMctsCsv csv1 = new MotherMctsCsv();
        csv1 = createMotherMcts(csv1);
        csv1.setWhomPhoneNo("15");
        csv1.setIdNo("15");
        csv1.setAbortion("None");
        csv1.setEntryType("8");
        csv1.setOutcomeNos("1");
        csv1.setName("testing");
        csv1.setLmpDate("2015-01-20 08:08:08");
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
    public void shouldUpdateBasedDifferentMsisdnSameMcts() throws Exception {
        logger.info("Inside createDifferentMsisdnSameMcts");
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
        csv1.setLmpDate("2015-01-22 08:08:08");
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
    public void shouldUpdateBasedDeleteOperation() throws Exception {
        logger.info("Inside  createDeleteOperation");
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
        csv1.setOperation("Del");
        MotherMctsCsv dbCsv1 = motherMctsCsvDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds); // Record update when different Msisdn and matching Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode())); //Operation Delete, Deactivate Subscription
        
        assertFalse(subscription.getStatus()==updateSubs.getStatus());
        assertTrue(subscription.getSubscriber().getName().equals(updateSubs.getSubscriber().getName()));
    }
    
}
