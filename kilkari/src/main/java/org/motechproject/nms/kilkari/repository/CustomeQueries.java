package org.motechproject.nms.kilkari.repository;

import javax.jdo.Query;

import org.motechproject.mds.query.SqlQueryExecution;
import org.motechproject.nms.kilkari.domain.ActiveUser;

public class CustomeQueries {

    /**
     * ActiveUserCountIncrementQuery class prepares a custom MDS query. The query
     * should increment activeUserCount.
     *
     */
    public static class ActiveUserCountIncrementQuery implements
    SqlQueryExecution<ActiveUser> {
        String incrementQuery = "update activeuser set activeusercount = activeusercount + 1";
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
        String decrementQuery = "update activeuser set activeusercount = activeusercount - 1";
        @Override
        public ActiveUser execute(Query query) {
            return (ActiveUser) query.execute();
        }

        @Override
        public String getSqlQuery() {
            return decrementQuery;
        }
    }
}
