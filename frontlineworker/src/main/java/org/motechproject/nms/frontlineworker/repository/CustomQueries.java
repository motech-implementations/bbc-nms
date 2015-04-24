package org.motechproject.nms.frontlineworker.repository;

import org.joda.time.DateTime;
import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;
import org.motechproject.nms.frontlineworker.enums.Status;
import org.motechproject.nms.frontlineworker.domain.Configuration;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jdo.Query;
import java.util.List;

/**
 * This class provides queries that will be used to fetch certain records from front line worker records saved in
 * database.
 */
@Repository
public class CustomQueries {


    public static class DeleteFrontLineWorkerQuery implements QueryExecution<List<FrontLineWorker>> {

        @Autowired
        ConfigurationService configurationService;

        @Override
        public List<FrontLineWorker> execute(Query query, InstanceSecurityRestriction restriction) {
            DateTime date = new DateTime();

            Configuration configuration = null;
            configuration = configurationService.getConfiguration();
            date = date.minusDays(configuration.getPurgeDate() - 1);
            query = query.getPersistenceManager().newQuery(FrontLineWorker.class);
            query.setFilter("(status == '"+ Status.INVALID+"') && invalidDate < date");
            query.declareParameters("java.util.Date date");
            query.deletePersistentAll(date.toDate());
            return null;
        }
    }



}
