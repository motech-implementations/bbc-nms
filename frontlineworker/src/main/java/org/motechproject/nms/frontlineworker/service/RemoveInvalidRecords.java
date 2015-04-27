package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This interface provides the service which will be used by batch scheduler to schedule jobs on frontLineWorker.
 */
@Service
public interface RemoveInvalidRecords {

    /**
     * This procedure deleted all the FrontLineWorker records from database which are marked as invalid
     */
    public void deleteInvalidFrontLineWorkerRecords();

    /**
     * This procedure returns the list of FrontLineWorkers which are marked as invalid in database for a specified
     * number of days. the number of days is a configurable parameter.
     *
     * @return list of invalid frontLineWorkers
     */
    public List<FrontLineWorker> invalidFrontLineWorkerList();
}
