package org.motechproject.nms.kilkari.it.event.handler;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.nms.kilkari.domain.*;
import org.motechproject.nms.kilkari.initializer.Initializer;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
/**
 * Verify that HelloWorldRecordService present, functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CsvMctsChildHandlerTestIT extends CommonStructure {
    
    private static Logger logger = LoggerFactory.getLogger(CsvMctsChildHandlerTestIT.class);
    @Test
    public void shouldCreateSubscriptionSubscriberTest() throws Exception {
        logger.info("Inside shouldCreateSubscriptionSubscriberTest");
        
        List<Long> uploadedIds = new ArrayList<Long>();
        CsvMctsChild csv = new CsvMctsChild();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("10000000031");
        csv.setIdNo("31");
        
        CsvMctsChild dbCsv = csvMctsChildDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); //create new record
        
        Subscription dbSubscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        Subscriber dbSubscriber = dbSubscription.getSubscriber();
        assertNotNull(dbSubscription);
        assertNotNull(dbSubscriber);
        assertTrue(dbSubscription.getChannel().equals(Channel.MCTS));
        assertTrue(dbSubscriber.getName().equals(csv.getMotherName()));
    } 
    
    @Test
    public void shouldUpdateBasedSameMsisdnDifferentMcts() throws Exception {
        logger.info("Inside shouldUpdateBasedSameMsisdnDifferentMcts");
        
        List<Long> uploadedIds = new ArrayList<Long>();
        CsvMctsChild csv = new CsvMctsChild();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("1000000032");
        csv.setIdNo("32");
        CsvMctsChild dbCsv = csvMctsChildDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        
        CsvMctsChild csv1 = new CsvMctsChild();
        csv1 = createChildMcts(csv1);
        csv1.setWhomPhoneNo("1000000032");
        csv1.setIdNo("33");
        CsvMctsChild dbCsv1 = csvMctsChildDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Record_Already Exist
        
        Subscription dbSubscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        assertNotNull(dbSubscription);
        dbSubscription = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode()));
        assertNull(dbSubscription);
    }
    
    @Test
    public void shouldUpdateBasedSameMsisdnSameMcts() throws Exception {
        logger.info("Inside shouldUpdateBasedSameMsisdnSameMcts");
        
        List<Long> uploadedIds = new ArrayList<Long>();
        CsvMctsChild csv = new CsvMctsChild();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("1000000034");
        csv.setIdNo("34");
        CsvMctsChild dbCsv = csvMctsChildDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        CsvMctsChild csv1 = new CsvMctsChild();
        csv1 = createChildMcts(csv1);
        csv1.setWhomPhoneNo("1000000034");
        csv1.setIdNo("34");
        csv1.setMotherName("testing");
        CsvMctsChild dbCsv1 = csvMctsChildDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Record update when matching Msisdn and Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode()));
        
        assertNotNull(subscription);
        assertNotNull(updateSubs);
        assertNotNull(subscription.getSubscriber());
        assertNotNull(updateSubs.getSubscriber());
        assertFalse(subscription.getSubscriber().getName().equals(updateSubs.getSubscriber().getName()));
    }
    
    @Test
    public void shouldUpdateBasedDifferentMsisdnSameMcts() throws Exception {
        logger.info("Inside shouldUpdateBasedDifferentMsisdnSameMcts");
        List<Long> uploadedIds = new ArrayList<Long>();
        CsvMctsChild csv = new CsvMctsChild();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("544444444444444444");
        csv.setIdNo("35");
        CsvMctsChild dbCsv = csvMctsChildDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        CsvMctsChild csv1 = new CsvMctsChild();
        csv1 = createChildMcts(csv1);
        csv1.setWhomPhoneNo("1000000036");
        csv1.setIdNo("35");
        csv1.setMotherName("testDifferentName");
        csv1.setBirthdate("2015-01-22 08:08:08");
        CsvMctsChild dbCsv1 = csvMctsChildDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Record update when different Msisdn and matching Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode()));
        
        assertNotNull(subscription);
        assertNotNull(updateSubs);
        assertNotNull(subscription.getSubscriber());
        assertNotNull(updateSubs.getSubscriber());
        assertFalse(subscription.getSubscriber().getName().equals(updateSubs.getSubscriber().getName()));
        assertFalse(subscription.getSubscriber().getDob().equals(updateSubs.getSubscriber().getDob()));
        assertFalse(subscription.getDeactivationReason() == updateSubs.getDeactivationReason());
    }
    
    @Test
    public void shouldUpdateBasedChildDeath() throws Exception {
        logger.info("Inside shouldUpdateBasedChildDeath");
        List<Long> uploadedIds = new ArrayList<Long>();
        CsvMctsChild csv = new CsvMctsChild();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("1000000051");
        csv.setIdNo("51");
        CsvMctsChild dbCsv = csvMctsChildDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        CsvMctsChild csv1 = new CsvMctsChild();
        csv1 = createChildMcts(csv1);
        csv1.setWhomPhoneNo("1000000052");
        csv1.setIdNo("51");
        csv1.setMotherName("testDifferentName");
        csv1.setEntryType("9");
        CsvMctsChild dbCsv1 = csvMctsChildDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Record update when different Msisdn and matching Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode()));
        
        assertNotNull(subscription);
        assertNotNull(updateSubs);
        assertNotNull(subscription.getSubscriber());
        assertNotNull(updateSubs.getSubscriber());
        assertFalse(subscription.getSubscriber().getName().equals(updateSubs.getSubscriber().getName()));
        assertFalse(subscription.getStatus()==updateSubs.getStatus());
        assertTrue(DeactivationReason.CHILD_DEATH==updateSubs.getDeactivationReason());
    }
    
    
    @Ignore
    public void shouldUpdateBasedDeleteOperation() throws Exception {
        logger.info("Inside  shouldUpdateBasedDeleteOperation");
        
        List<Long> uploadedIds = new ArrayList<Long>();
        CsvMctsChild csv = new CsvMctsChild();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("41");
        csv.setIdNo("42");
        CsvMctsChild dbCsv = csvMctsChildDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        CsvMctsChild csv1 = new CsvMctsChild();
        csv1 = createChildMcts(csv1);
        csv1.setWhomPhoneNo("43");
        csv1.setIdNo("42");
        CsvMctsChild dbCsv1 = csvMctsChildDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Record update when different Msisdn and matching Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode())); //Operation Delete, Deactivate Subscription
        
        assertFalse(subscription.getStatus()==updateSubs.getStatus());
        assertTrue(subscription.getSubscriber().getName().equals(updateSubs.getSubscriber().getName()));
    }
    
    @Test
    public void shouldUpdateBasedDiffMsisdnDiffChildMctsSameMotherMcts() throws Exception {
        logger.info("Inside  shouldUpdateBasedDiffMsisdnDiffChildMctsSameMotherMcts");
        
        List<Long> uploadedIds = new ArrayList<Long>();
        CsvMctsMother csv = new CsvMctsMother();
        csv = createMotherMcts(csv);
        csv.setWhomPhoneNo("1000000037");
        csv.setIdNo("37");
        CsvMctsMother dbCsv = csvMctsMotherDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsMotherHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        
        CsvMctsChild childCsv = new CsvMctsChild();
        childCsv = createChildMcts(childCsv);
        childCsv.setWhomPhoneNo("1000000038");
        childCsv.setIdNo("38");
        childCsv.setMotherId("37");
        CsvMctsChild dbCsv1 = csvMctsChildDataService.create(childCsv);
        uploadedIds.add(dbCsv1.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(childCsv.getIdNo(), Long.parseLong(childCsv.getStateCode()));
        
        assertNotNull(subscription);
        assertNotNull(updateSubs);
        assertFalse(subscription.getStatus()==updateSubs.getStatus());
        assertTrue(subscription.getDeactivationReason()==DeactivationReason.PACK_CHANGED);
        
    }
    
    @Test
    public void shouldUpdateBasedDiffMsisdnDiffChildMctsSameMotherMctsTrueChildDeath() throws Exception {
        logger.info("Inside  shouldUpdateBasedDiffMsisdnDiffChildMctsSameMotherMctsTrueChildDeath");
        
        List<Long> uploadedIds = new ArrayList<Long>();
        CsvMctsMother csv = new CsvMctsMother();
        csv = createMotherMcts(csv);
        csv.setWhomPhoneNo("1000000039");
        csv.setIdNo("39");
        CsvMctsMother dbCsv = csvMctsMotherDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsMotherHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        CsvMctsChild childCsv = new CsvMctsChild();
        childCsv = createChildMcts(childCsv);
        childCsv.setWhomPhoneNo("1000000040");
        childCsv.setIdNo("40");
        childCsv.setMotherId("39");
        childCsv.setEntryType("9");
        CsvMctsChild dbCsv1 = csvMctsChildDataService.create(childCsv);
        uploadedIds.add(dbCsv1.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds);
        uploadedIds.clear();
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(childCsv.getMotherId(), Long.parseLong(childCsv.getStateCode()));
        
        assertNotNull(subscription);
        assertNotNull(updateSubs);
        assertFalse(subscription.getMsisdn().equals(updateSubs.getMsisdn()));
    }
    
    
    @Test
    public void shouldDecactivateMotherAndDeleteSubscriberBasedDiffMsisdnSameChildMcts() throws Exception {
        logger.info("Inside  shouldDecactivateMotherAndDeleteSubscriberBasedDiffMsisdnSameChildMcts");
        
        List<Long> uploadedIds = new ArrayList<Long>();
        CsvMctsMother csv = new CsvMctsMother();
        csv = createMotherMcts(csv);
        csv.setWhomPhoneNo("1000000042");
        csv.setIdNo("42");
        CsvMctsMother dbCsv = csvMctsMotherDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsMotherHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        
        CsvMctsChild childCsv = new CsvMctsChild();
        childCsv = createChildMcts(childCsv);
        childCsv.setWhomPhoneNo("1000000043");
        childCsv.setIdNo("43");
        childCsv.setEntryType("1");
        CsvMctsChild dbCsv1 = csvMctsChildDataService.create(childCsv);
        uploadedIds.add(dbCsv1.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds);
        uploadedIds.clear();
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(childCsv.getIdNo(), Long.parseLong(childCsv.getStateCode()));
        
        
        CsvMctsChild childCsv2 = new CsvMctsChild();
        childCsv2 = createChildMcts(childCsv2);
        childCsv2.setWhomPhoneNo("10000000401");
        childCsv2.setIdNo("43");
        childCsv2.setMotherId("42");
        childCsv2.setEntryType("1");
        CsvMctsChild dbCsv2 = csvMctsChildDataService.create(childCsv2);
        uploadedIds.add(dbCsv2.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds);
        uploadedIds.clear();
        
        Subscription subscriptionFirst = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        assertNotNull(subscriptionFirst.getSubscriber());
        assertTrue(subscriptionFirst.getMctsId().equals(csv.getIdNo()));
        assertTrue(subscriptionFirst.getStatus() == Status.DEACTIVATED);
        assertTrue(subscriptionFirst.getDeactivationReason() == DeactivationReason.PACK_CHANGED);
        assertTrue(subscriptionFirst.getSubscriber().getId() == updateSubs.getSubscriber().getId());
        assertNotNull(updateSubs);
    }
    
    
    @Test
    public void shouldDecactivateMotherAndDeleteSubscriberBasedSameMsisdn() throws Exception {
        logger.info("Inside  shouldDecactivateMotherAndDeleteSubscriberBasedSameMsisdn");
        
        List<Long> uploadedIds = new ArrayList<Long>();
        CsvMctsMother csv = new CsvMctsMother();
        csv = createMotherMcts(csv);
        csv.setWhomPhoneNo("5111111111111");
        csv.setIdNo("45");
        CsvMctsMother dbCsv = csvMctsMotherDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsMotherHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        Subscription subscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        CsvMctsMother csv1 = new CsvMctsMother();
        csv1 = createMotherMcts(csv1);
        csv1.setWhomPhoneNo("1000000045");
        csv1.setIdNo("45");
        csv1.setName("testDifferentName");
        csv1.setLmpDate("2015-01-22 08:08:08");
        CsvMctsMother dbCsv1 = csvMctsMotherDataService.create(csv1);
        uploadedIds.add(dbCsv1.getId());
        callCsvMctsMotherHandlerSuccessEvent(uploadedIds); // Record update when different Msisdn and matching Mctsid
        Subscription updateSubs = subscriptionService.getSubscriptionByMctsIdState(csv1.getIdNo(), Long.parseLong(csv1.getStateCode()));       
        
        CsvMctsChild childCsv = new CsvMctsChild();
        childCsv = createChildMcts(childCsv);
        childCsv.setWhomPhoneNo("1000000046");
        childCsv.setIdNo("46");
        CsvMctsChild dbChildCsv = csvMctsChildDataService.create(childCsv);
        uploadedIds.add(dbChildCsv.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds);
        uploadedIds.clear();
        Subscription updatedChildSubs = subscriptionService.getSubscriptionByMctsIdState(childCsv.getIdNo(), Long.parseLong(childCsv.getStateCode()));
        
        
        CsvMctsChild childCsv2 = new CsvMctsChild();
        childCsv2 = createChildMcts(childCsv2);
        childCsv2.setWhomPhoneNo("1000000046");
        childCsv2.setIdNo("46");
        childCsv2.setMotherId("45");
        childCsv2.setEntryType("1");
        CsvMctsChild dbCsv2 = csvMctsChildDataService.create(childCsv2);
        uploadedIds.add(dbCsv2.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds);
        uploadedIds.clear();
        
        Subscription subscriptionFirst = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        
        assertNotNull(subscriptionFirst.getSubscriber());
        assertTrue(subscriptionFirst.getMctsId().equals(csv.getIdNo()));
        assertTrue(subscriptionFirst.getStatus() == Status.DEACTIVATED);
        assertTrue(subscriptionFirst.getDeactivationReason() == DeactivationReason.PACK_SCHEDULE_CHANGED);
        assertTrue(subscriptionFirst.getSubscriber().getId() == updatedChildSubs.getSubscriber().getId());
        assertNotNull(updatedChildSubs);
    }
    
    
    @Test
    public void checkMaximumAllowedBeneficery() throws Exception {
        logger.info("Inside checkMaximumAllowedBeneficery");
        
        ActiveSubscriptionCount activeUser = activeSubscriptionCountDataService.findActiveSubscriptionCountByIndex(1L);
        activeUser.setCount(9718577L);
        activeUser = activeSubscriptionCountDataService.update(activeUser);
        
        List<Long> uploadedIds = new ArrayList<Long>();
        CsvMctsChild csv = new CsvMctsChild();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("10000000048");
        csv.setIdNo("48");
        
        CsvMctsChild dbCsv = csvMctsChildDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); //create new record
        
        Subscription dbSubscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        assertNull(dbSubscription);
        
        activeUser.setCount(0L);
        activeSubscriptionCountDataService.update(activeUser);
        
    } 
    
    @Test
    public void testRecordRejectedIfStillBirthTrue() throws Exception {
        logger.info("Inside testRecordRejectedIfAbortionTrue");
        
        List<Long> uploadedIds = new ArrayList<Long>();
        CsvMctsChild csv = new CsvMctsChild();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("10000000049");
        csv.setIdNo("49");
        csv.setEntryType("9");
        
        CsvMctsChild dbCsv = csvMctsChildDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); //create new record
        
        Subscription dbSubscription = subscriptionService.getSubscriptionByMctsIdState(csv.getIdNo(), Long.parseLong(csv.getStateCode()));
        assertNull(dbSubscription);
        
    } 
    
    
    @Test
    public void testUploadedIdNotInDatabase() throws Exception {
        logger.info("Inside  testUploadedIdNotInDatabase");
        
        List<Long> uploadedIds = new ArrayList<Long>();
        Long uploadedId = new Random().nextLong();
        uploadedIds.add(uploadedId);
        callCsvMctsChildHandlerSuccessEvent(uploadedIds);
        assertNull(csvMctsMotherDataService.findById(uploadedId));
        
    }
    

    @Test
    public void testScheduledSubscriptionApi(){
        
        logger.info("Inside createSameMsisdnSameMcts");
        
        List<Long> uploadedIds = new ArrayList<Long>();
        
        CsvMctsChild csv = new CsvMctsChild();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("1000000032");
        csv.setIdNo("32");
        DateTime date = new DateTime().minusDays(10);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        csv.setBirthdate(dtf.print(date));
        CsvMctsChild dbCsv = csvMctsChildDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        
        csv = new CsvMctsChild();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("1000000033");
        csv.setIdNo("33");
        date = new DateTime();
        dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        csv.setBirthdate(dtf.print(date));
        dbCsv = csvMctsChildDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        
        csv = new CsvMctsChild();
        csv = createChildMcts(csv);
        csv.setWhomPhoneNo("1000000034");
        csv.setIdNo("34");
        date = new DateTime().minusDays(3);
        dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        csv.setBirthdate(dtf.print(date));
        dbCsv = csvMctsChildDataService.create(csv);
        uploadedIds.add(dbCsv.getId());
        callCsvMctsChildHandlerSuccessEvent(uploadedIds); // Created New Record
        uploadedIds.clear();
        
        List<Subscription> list = subscriptionService.getScheduledSubscriptions();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        if(Initializer.DEFAULT_NUMBER_OF_MSG_PER_WEEK==2){
            assertEquals(list.size(), 3);
        }
        
        if(Initializer.DEFAULT_NUMBER_OF_MSG_PER_WEEK==1){
            assertEquals(list.size(), 1);
        }
        
    }
    
    @Test
    public void testForNumberOfMessagePerWeekIs2(){
        Configuration configuration = configurationService.getConfiguration();
        if(Initializer.DEFAULT_NUMBER_OF_MSG_PER_WEEK==1) {
            configuration.setNumMsgPerWeek(2);
        } else {
            configuration.setNumMsgPerWeek(1);
        }
        configuration = configurationDateService.update(configuration);
        
        testScheduledSubscriptionApi();
        
        configuration.setNumMsgPerWeek(Initializer.DEFAULT_NUMBER_OF_MSG_PER_WEEK);
        configurationDateService.update(configuration);
        
    }
    
}
