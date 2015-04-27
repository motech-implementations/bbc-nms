package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This interface provides the service which will be used by batch scheduler to schedule jobs on frontLineWorker.
 */
@Service
public interface RemoveInvalidRecords {

    public void deleteInvalidFrontLineWorkerRecords();

    public List<FrontLineWorker> invalidFrontLineWorkerList();
}
