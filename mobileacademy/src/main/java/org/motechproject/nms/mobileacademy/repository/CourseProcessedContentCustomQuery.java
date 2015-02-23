package org.motechproject.nms.mobileacademy.repository;

import java.util.List;

import javax.jdo.Query;

import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;

/**
 * Class contains custom query related to course processed content.
 *
 */
public class CourseProcessedContentCustomQuery {

    /**
     * LlcListQueryExecutionImpl class prepares a custom MDS query. The query
     * should return list of distinct LLC.
     *
     *
     */
    public class LlcListQueryExecutionImpl implements
            QueryExecution<List<Integer>> {

        @Override
        public List<Integer> execute(Query query,
                InstanceSecurityRestriction restriction) {
            query.setResult("DISTINCT languageLocationCode");
            return (List<Integer>) query.execute();
        }
    }

    /**
     * DeleteProcessedContentQueryExecutionImpl class prepares a custom MDS
     * query. The query delete records on the basis of LLC.
     *
     *
     */
    public class DeleteProcessedContentQueryExecutionImpl implements
            QueryExecution<Long> {

        private int languageLocationCode;

        /**
         * constructor with all arguments.
         * 
         * @param languageLocationCode LLC identifier
         */
        public DeleteProcessedContentQueryExecutionImpl(int languageLocationCode) {
            this.languageLocationCode = languageLocationCode;
        }

        @Override
        public Long execute(Query query, InstanceSecurityRestriction restriction) {
            query.setFilter("languageLocationCode ==" + languageLocationCode);
            return query.deletePersistentAll();
        }
    }

}