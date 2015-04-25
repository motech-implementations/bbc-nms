package org.motechproject.nms.kilkari.repository;

import org.joda.time.DateTime;
import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.nms.kilkari.commons.Constants;
import org.motechproject.nms.kilkari.domain.*;
import org.motechproject.nms.kilkari.initializer.Initializer;

import javax.jdo.Query;
import java.util.List;

public class CustomQueries {

    /**
     * Query to find list of Active and Pending subscription packs for given msisdn.
     */
    public static class ActiveSubscriptionQuery implements QueryExecution<List<SubscriptionPack>> {
        private String msisdn;
        private String resultParamName;

        public ActiveSubscriptionQuery(String msisdn, String resultParamName) {
            this.msisdn = msisdn;
            this.resultParamName = resultParamName;
        }

        /**
         * This method executes the query passed.
         * @param query to be executed
         * @param restriction
         * @return List of distinct subscription packs
         */
        @Override
        public List<SubscriptionPack> execute(Query query, InstanceSecurityRestriction restriction) {
            query.setFilter("msisdn == '" + msisdn + "' && (status == '" + Status.ACTIVE + "' ||" + " status == '" + Status.PENDING_ACTIVATION + "')");
            query.setResult("DISTINCT " + resultParamName);
            return (List<SubscriptionPack>) query.execute();
        }
    }
    
    /**
     * This class is used to delete SubscriptionMeasure having passed subscriptionIds
     */
    public static class DeleteSubscriptionMeasureQuery implements QueryExecution<Long> {

        private List<Long> subscriptionIds = null;
        
        public DeleteSubscriptionMeasureQuery(List<Long> subscriptionIds) {
            this.subscriptionIds = subscriptionIds;
        }
        
        /**
         * This method executes the query passed and delete filtered SubscriptionMeasure.
         * 
         * @param query to be executed
         * @param restriction
         * @return count of SubscriptionMeasure records deleted
         */
        @Override
        public Long execute(Query query, InstanceSecurityRestriction restriction) {
            query = query.getPersistenceManager().newQuery(SubscriptionMeasure.class);
            query.setFilter(":p1.contains(subscriptionId)");
            return query.deletePersistentAll(subscriptionIds);
        }
    }

    /**
     * This class is used to delete subscriptions
     * whose status is completed or deactivated n days earlier.
     */
    public static class DeleteSubscriptionQuery implements QueryExecution<Long> {

        private Integer expiredSubscriptionAgeDays = 0;
        
        public DeleteSubscriptionQuery(Integer expiredSubscriptionAgeDays) {
            this.expiredSubscriptionAgeDays = expiredSubscriptionAgeDays;
        }
        
        /**
         * This method executes the query passed and delete filtered subscription.
         * 
         * @param query to be executed
         * @param restriction
         * @return count of Subscription records deleted
         */
        @Override
        public Long execute(Query query, InstanceSecurityRestriction restriction) {
            DateTime date = new DateTime();
            date = date.minusDays(expiredSubscriptionAgeDays-1);
            query = query.getPersistenceManager().newQuery(Subscription.class);
            query.setFilter("(status == '"+Status.COMPLETED+"' || status == '"+Status.DEACTIVATED+"') && completionOrDeactivationDate < date");
            query.declareParameters("java.util.Date date");
            return query.deletePersistentAll(date.toDate());
        }
    }

    /**
     * This class is used to delete subscriber 
     * who doesn't have any subscription
     */
    public static class DeleteSubscriberQuery implements QueryExecution<Long> {

        /**
         * This method executes the query passed and delete filtered subscriber.
         * 
         * @param query to be executed
         * @param restriction
         * @return Count of Subscriber records deleted.
         */
        @Override
        public Long execute(Query query, InstanceSecurityRestriction restriction) {
            query =  query.getPersistenceManager().newQuery(Subscriber.class);
            query.setFilter("subscriptionList.isEmpty()");
            return query.deletePersistentAll();
        }
    }

    /**
     * This class is used to find scheduled subscription 
     */
    public static class FindScheduledSubscription implements QueryExecution<List<Subscription>> {

        /**
         * This method executes the query passed and return 
         * list of subscription whom message is to send today
         * 
         * @param query to be executed
         * @param restriction
         * @return List of scheduled subscription
         */
        @Override
        public List<Subscription> execute(Query query, InstanceSecurityRestriction restriction) {
            DateTime date = new DateTime();
            long currDateInMillis = date.toDateMidnight().getMillis();
            if(Initializer.DEFAULT_NUMBER_OF_MSG_PER_WEEK == Constants.FIRST_MSG_OF_WEEK) {
                query.setFilter("(status == '"+Status.ACTIVE+"' || status == '"+Status.PENDING_ACTIVATION+"') && (currDateInMillis-startDate) >= 0 && (((currDateInMillis-startDate)/day) % " + Constants.DAYS_IN_WEEK + " == 0)");
            } else {
                query.setFilter("(status == '"+Status.ACTIVE+"' || status == '"+Status.PENDING_ACTIVATION+"') && (currDateInMillis-startDate) >= 0 && ((((currDateInMillis-startDate)/day) % " + Constants.DAYS_IN_WEEK + " == 0) || (((currDateInMillis-startDate)/day) % " + Constants.DAYS_IN_WEEK + " == 3))");
            }
            query.declareParameters("Long currDateInMillis, Integer day");
            return (List<Subscription>) query.execute(currDateInMillis, Constants.MILLIS_IN_DAY);
        }
    }
    
    /**
     * This class is used to delete subscriptions
     * whose status is completed or deactivated n days earlier.
     */
    public static class SubscriptionIdOfNDaysEarlierSubscription implements QueryExecution<List<Long>> {

        private Integer expiredSubscriptionAgeDays = 0;
        
        public SubscriptionIdOfNDaysEarlierSubscription(Integer expiredSubscriptionAgeDays) {
            this.expiredSubscriptionAgeDays = expiredSubscriptionAgeDays;
        }
        
        /**
         * This method executes the query passed and delete filtered subscription.
         * 
         * @param query to be executed
         * @param restriction
         * @return count of Subscription records deleted
         */
        @Override
        public List<Long> execute(Query query, InstanceSecurityRestriction restriction) {
            DateTime date = new DateTime();
            date = date.minusDays(expiredSubscriptionAgeDays-1);
            query = query.getPersistenceManager().newQuery(Subscription.class);
            query.setFilter("(status == '"+Status.COMPLETED+"' || status == '"+Status.DEACTIVATED+"') && completionOrDeactivationDate < date");
            query.declareParameters("java.util.Date date");
            query.setResult("DISTINCT id");
            return (List<Long>) query.execute(date.toDate());
        }
    }

}
