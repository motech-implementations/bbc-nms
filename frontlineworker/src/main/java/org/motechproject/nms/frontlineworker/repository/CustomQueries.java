package org.motechproject.nms.frontlineworker.repository;

import org.joda.time.DateTime;
import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.springframework.stereotype.Repository;

import javax.jdo.Query;
import java.util.List;

/**
 * Created by abhishek on 21/4/15.
 */
@Repository
public class CustomQueries {


    public static class DeleteFrontLineWorkerQuery implements QueryExecution<List<FrontLineWorker>> {

        @Override
        public List<FrontLineWorker> execute(Query query, InstanceSecurityRestriction restriction) {
            DateTime date = new DateTime();
            date = date.minusDays(41);
            query = query.getPersistenceManager().newQuery(FrontLineWorker.class);
            query.setFilter("(status == '"+ Status.INVALID+"') && modificationDate < date");
            query.declareParameters("java.util.Date date");
            query.deletePersistentAll(date.toDate());
            return null;
        }
    }



}
