package org.motechproject.nms.kilkari.repository;

import javax.jdo.Query;

import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.query.SqlQueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.nms.kilkari.domain.ActiveUser;
import org.motechproject.nms.kilkari.domain.Status;
import org.motechproject.nms.kilkari.domain.SubscriptionPack;

import java.util.List;

public class CustomeQueries {

    /**
     * ActiveUserCountIncrementQuery class prepares a custom MDS query. The query
     * should increment activeUserCount.
     *
     */
    public static class ActiveUserCountIncrementQuery implements
    SqlQueryExecution<ActiveUser> {
        String incrementQuery = "update KILKARI_ACTIVEUSER set activeUserCount = activeUserCount + 1 where id = 1; ";
        @Override
        public ActiveUser execute(Query query) {
            return (ActiveUser) query.execute();
        }

        @Override
        public String getSqlQuery() {
            return incrementQuery;
        }


    }

    /**
     * ActiveUserCountIncrementQuery class prepares a custom MDS query. The query
     * should decrement activeUserCount.
     *
     */
    public static class ActiveUserCountDecrementQuery implements
    SqlQueryExecution<ActiveUser> {
        String decrementQuery = "update KILKARI_ACTIVEUSER set activeUserCount = activeUserCount - 1 where id = 1; ";
        @Override
        public ActiveUser execute(Query query) {
            return (ActiveUser) query.execute();
        }

        @Override
        public String getSqlQuery() {
            return decrementQuery;
        }
    }

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

        @Override
        public List<SubscriptionPack> execute(Query query, InstanceSecurityRestriction restriction) {
            query.setFilter("msisdn == '" + msisdn + "'");
            query.setFilter("status == " + Status.ACTIVE + "or" + " status == " + Status.PENDING_ACTIVATION);
            query.setResult("DISTINCT " + resultParamName);
            return null;
        }
    }
}
