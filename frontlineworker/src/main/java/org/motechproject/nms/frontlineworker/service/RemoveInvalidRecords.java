package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by abhishek on 21/4/15.
 */
@Service
public interface RemoveInvalidRecords {

    public void deleteInvalidFrontLineWorkerRecords();

    public List<FrontLineWorker> invalidFrontLineWorkerList();
}
