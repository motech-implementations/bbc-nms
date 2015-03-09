package org.motechproject.nms.kilkari.it.event.handler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;
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
public class ChildMctsCsvHandlerTestIT extends CommonStructure {
    
    private static Logger logger = LoggerFactory.getLogger(ChildMctsCsvHandlerTestIT.class);
    @Test
    public void shouldCreateSubscriptionSubscriberTest() throws Exception {
        logger.info("Inside createSubscriptionSubscriberTest");
        setUp();
        
        List<Long> uploadedIds = new ArrayList<Long>();
        ChildMctsCsv csv = new ChildMctsCsv();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("31");
        csv.setIdNo("31");
        
        ChildMctsCsv dbCsv = childMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callChildMctsCsvHandlerSuccessEvent(uploadedIds); //create new record
        
        Subscription dbSubscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        Subscriber dbSubscriber = dbSubscription.getSubscriber();
        assertNotNull(dbSubscription);
        assertNotNull(dbSubscriber);
        assertTrue(dbSubscription.getChannel().equals(Channel.MCTS));
        assertTrue(dbSubscriber.getName().equals(csv.getMotherName()));
        assertTrue(dbSubscriber.getState().getStateCode().toString().equals(csv.getStateCode()));
    } 
    
    @Test
    public void shouldUpdateBasedSameMsisdnDifferentMcts() throws Exception {
        logger.info("Inside createSameMsisdnDifferentMcts");
        setUp();
        
        List<Long> uploadedIds = new ArrayList<Long>();
        ChildMctsCsv csv = new ChildMctsCsv();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("32");
        csv.setIdNo("32");
        ChildMctsCsv dbCsv = childMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callChildMctsCsvHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        
        ChildMctsCsv csv1 = new ChildMctsCsv();
        csv1 = createChildMcts(csv1);
        csv1.setWhomPhoneNo("32");
        csv1.setIdNo("33");
        ChildMctsCsv dbCsv1 = childMctsCsvDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callChildMctsCsvHandlerSuccessEvent(uploadedIds); // Record_Already Exist
        
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
        ChildMctsCsv csv = new ChildMctsCsv();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("34");
        csv.setIdNo("34");
        ChildMctsCsv dbCsv = childMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callChildMctsCsvHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        ChildMctsCsv csv1 = new ChildMctsCsv();
        csv1 = createChildMcts(csv1);
        csv1.setWhomPhoneNo("34");
        csv1.setIdNo("34");
        csv1.setMotherName("testing");
        csv1.setBirthdate("2015-01-20 08:08:08");
        ChildMctsCsv dbCsv1 = childMctsCsvDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callChildMctsCsvHandlerSuccessEvent(uploadedIds); // Record update when matching Msisdn and Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode()));
        
        assertNotNull(subscription);
        assertNotNull(updateSubs);
        assertNotNull(subscription.getSubscriber());
        assertNotNull(updateSubs.getSubscriber());
        assertFalse(subscription.getSubscriber().getName().equals(updateSubs.getSubscriber().getName()));
        assertFalse(subscription.getSubscriber().getDob().equals(updateSubs.getSubscriber().getDob()));
    }
    
    @Test
    public void shouldUpdateBasedDifferentMsisdnSameMcts() throws Exception {
        logger.info("Inside createDifferentMsisdnSameMcts");
        setUp();
        List<Long> uploadedIds = new ArrayList<Long>();
        ChildMctsCsv csv = new ChildMctsCsv();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("35");
        csv.setIdNo("35");
        ChildMctsCsv dbCsv = childMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callChildMctsCsvHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        ChildMctsCsv csv1 = new ChildMctsCsv();
        csv1 = createChildMcts(csv1);
        csv1.setWhomPhoneNo("36");
        csv1.setIdNo("35");
        csv1.setMotherName("testDifferentName");
        csv1.setBirthdate("2015-01-22 08:08:08");
        ChildMctsCsv dbCsv1 = childMctsCsvDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callChildMctsCsvHandlerSuccessEvent(uploadedIds); // Record update when different Msisdn and matching Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode()));
        
        assertNotNull(subscription);
        assertNotNull(updateSubs);
        assertNotNull(subscription.getSubscriber());
        assertNotNull(updateSubs.getSubscriber());
        assertFalse(subscription.getSubscriber().getName().equals(updateSubs.getSubscriber().getName()));
        assertFalse(subscription.getSubscriber().getDob().equals(updateSubs.getSubscriber().getDob()));
    }
    
    @Test
    public void shouldUpdateBasedChildDeath() throws Exception {
        logger.info("Inside createDifferentMsisdnSameMcts");
        setUp();
        List<Long> uploadedIds = new ArrayList<Long>();
        ChildMctsCsv csv = new ChildMctsCsv();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("51");
        csv.setIdNo("51");
        ChildMctsCsv dbCsv = childMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callChildMctsCsvHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        ChildMctsCsv csv1 = new ChildMctsCsv();
        csv1 = createChildMcts(csv1);
        csv1.setWhomPhoneNo("52");
        csv1.setIdNo("51");
        csv1.setMotherName("testDifferentName");
        csv1.setEntryType("9");
        ChildMctsCsv dbCsv1 = childMctsCsvDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callChildMctsCsvHandlerSuccessEvent(uploadedIds); // Record update when different Msisdn and matching Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode()));
        
        assertNotNull(subscription);
        assertNotNull(updateSubs);
        assertNotNull(subscription.getSubscriber());
        assertNotNull(updateSubs.getSubscriber());
        assertFalse(subscription.getSubscriber().getName().equals(updateSubs.getSubscriber().getName()));
        assertFalse(subscription.getStatus()==updateSubs.getStatus());
    }
    
    
    @Test
    public void shouldUpdateBasedDeleteOperation() throws Exception {
        logger.info("Inside  createDeleteOperation");
        setUp();
        
        List<Long> uploadedIds = new ArrayList<Long>();
        ChildMctsCsv csv = new ChildMctsCsv();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("41");
        csv.setIdNo("42");
        ChildMctsCsv dbCsv = childMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callChildMctsCsvHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        ChildMctsCsv csv1 = new ChildMctsCsv();
        csv1 = createChildMcts(csv1);
        csv1.setWhomPhoneNo("43");
        csv1.setIdNo("42");
        csv1.setOperation("Del");
        ChildMctsCsv dbCsv1 = childMctsCsvDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callChildMctsCsvHandlerSuccessEvent(uploadedIds); // Record update when different Msisdn and matching Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode())); //Operation Delete, Deactivate Subscription
        
        assertFalse(subscription.getStatus()==updateSubs.getStatus());
        assertTrue(subscription.getSubscriber().getName().equals(updateSubs.getSubscriber().getName()));
    }
    
    @Test
    public void shouldUpdateBasedDiffMsisdnDiffChildMctsSameMotherMcts() throws Exception {
        logger.info("Inside  createDeleteOperation");
        setUp();
        
        List<Long> uploadedIds = new ArrayList<Long>();
        MotherMctsCsv csv = new MotherMctsCsv();
        csv = createMotherMcts(csv);
        csv.setWhomPhoneNo("37");
        csv.setIdNo("37");
        MotherMctsCsv dbCsv = motherMctsCsvDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callMotherMctsCsvHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        
        ChildMctsCsv childCsv = new ChildMctsCsv();
        childCsv = createChildMcts(childCsv);
        childCsv.setWhomPhoneNo("38");
        childCsv.setIdNo("38");
        childCsv.setMotherId("37");
        ChildMctsCsv dbCsv1 = childMctsCsvDataService.create(childCsv);
        uploadedIds.add(dbCsv1.getId());
        callChildMctsCsvHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(childCsv.getIdNo(), Long.parseLong(childCsv.getStateCode()));
        
        assertNotNull(subscription);
        assertNotNull(updateSubs);
        assertFalse(subscription.getStatus()==updateSubs.getStatus());
        assertFalse(subscription.getPackName().equals(updateSubs.getPackName()));
    }
    
   
}
