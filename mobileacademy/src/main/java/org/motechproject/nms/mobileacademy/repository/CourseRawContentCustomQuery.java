package org.motechproject.nms.mobileacademy.repository;

import java.util.List;

import javax.jdo.Query;

import org.motechproject.mds.query.QueryExecution;
import org.motechproject.mds.util.InstanceSecurityRestriction;

/**
 * Class contains custom query related to course raw content.
 *
 */
public class CourseRawContentCustomQuery {

    /**
     * LlcListQueryExecutionImpl class prepares a custom MDS query. The query
     * should return list of distinct LLC id based on operation.
     *
     *
     */
    public class LlcListQueryExecutionImpl implements
            QueryExecution<List<String>> {

        private String operation;

        /**
         * Constructor with all arguments.
         * 
         * @param operation i.e ADD/MOD/DEL
         */
        public LlcListQueryExecutionImpl(String operation) {
            this.operation = operation;
        }

        @Override
        public List<String> execute(Query query,
                InstanceSecurityRestriction restriction) {
            query.setFilter("operation == '" + operation + "'");
            query.setResult("DISTINCT languageLocationCode");
            return (List<String>) query.execute();
        }
    }

    /**
     * DeleteRawContentQueryExecutionImpl class prepares a custom MDS query. The
     * query delete records on the basis of LLC and operation.
     *
     *
     */
    public class DeleteRawContentQueryExecutionImpl implements
            QueryExecution<Long> {

        private String operation;

        private String languageLocationCode;

        /**
         * constructor with all arguments.
         * 
         * @param languageLocationCode LLC identifier
         * @param operation i.e ADD/MOD/DEL
         */
        public DeleteRawContentQueryExecutionImpl(String languageLocationCode,
                String operation) {
            this.languageLocationCode = languageLocationCode;
            this.operation = operation;
        }

        @Override
        public Long execute(Query query, InstanceSecurityRestriction restriction) {
            query.setFilter("operation == '" + operation
                    + "' && languageLocationCode == '" + languageLocationCode
                    + "'");
            return query.deletePersistentAll();
        }
    }

}