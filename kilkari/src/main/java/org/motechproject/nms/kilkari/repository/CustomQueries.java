package org.motechproject.nms.kilkari.repository;

import java.util.List;

import javax.jdo.Query;

import org.joda.time.DateTime;
import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;
import org.motechproject.nms.kilkari.initializer.Initializer;

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
            org.joda.time.DateTime date = new DateTime();
            query.setFilter("Days.daysBetween(date, date)");
            query.declareImports("org.joda.time.Days");
            query.declareParameters("org.joda.time.DateTime date");
            return (List<SubscriptionPack>) query.execute();
        }
    }

    public static class DeleteSubscriptionQuery implements QueryExecution<List<Subscription>> {

        @Override
        public List<Subscription> execute(Query query, InstanceSecurityRestriction restriction) {
            DateTime date = new DateTime();
            date = date.minusDays(41);
            query = query.getPersistenceManager().newQuery(Subscription.class);
            query.setFilter("(status == '"+Status.COMPLETED+"' || status == '"+Status.DEACTIVATED+"') && modificationDate < date");
            query.declareParameters("java.util.Date date");
            query.deletePersistentAll(date.toDate());
            return null;
        }
    }

    public static class DeleteSubscriberQuery implements QueryExecution<List<Subscriber>> {

        @Override
        public List<Subscriber> execute(Query query, InstanceSecurityRestriction restriction) {
            query =  query.getPersistenceManager().newQuery(Subscriber.class);
            query.setFilter("subscriptionList.isEmpty()");
            query.deletePersistentAll();
            return null;

        }
    }

    public static class FindScheduledSubscription implements QueryExecution<List<Subscription>> {

        @Override
        public List<Subscription> execute(Query query, InstanceSecurityRestriction restriction) {
            DateTime date = new DateTime();
            long day = 1000*60*60*24; 
            long currDateInMillis = date.toDateMidnight().getMillis();
            if(Initializer.DEFAULT_NUMBER_OF_MSG_PER_WEEK == 1) {
                query.setFilter("(status == '"+Status.ACTIVE+"' || status == '"+Status.PENDING_ACTIVATION+"') && (currDateInMillis-startDate)>=0 && (((currDateInMillis-startDate)/day)%7 == 0)");
            } else {
                query.setFilter("(status == '"+Status.ACTIVE+"' || status == '"+Status.PENDING_ACTIVATION+"') && (currDateInMillis-startDate)>=0 && ((((currDateInMillis-startDate)/day)%7 == 0) || (((currDateInMillis-startDate)/day)%7 == 3))");
            }
            query.declareParameters("Long currDateInMillis, Integer day");
            return (List<Subscription>) query.execute(currDateInMillis, day);
        }
    }

}
