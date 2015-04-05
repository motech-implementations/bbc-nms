package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.repository.CustomQueries;
import org.motechproject.nms.frontlineworker.repository.FrontLineWorkerRecordDataService;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.motechproject.nms.frontlineworker.service.RemoveInvalidRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class provides the implementation to RemoveInvalidRecordsService which will be used by batch
 * scheduler to schedule jobs on frontLineWorker.
 */
@Service("removeInvalidRecordsService")
public class RemoveInvalidRecordsImpl implements RemoveInvalidRecords {

    FrontLineWorkerRecordDataService frontLineWorkerRecordDataService;

    FrontLineWorkerService frontLineWorkerService;

    private static Logger logger = LoggerFactory.getLogger(RemoveInvalidRecordsImpl.class);

    @Autowired
    public RemoveInvalidRecordsImpl(FrontLineWorkerRecordDataService frontLineWorkerRecordDataService, FrontLineWorkerService frontLineWorkerService) {
        this.frontLineWorkerRecordDataService = frontLineWorkerRecordDataService;
        this.frontLineWorkerService = frontLineWorkerService;
    }

    @Override
    public List<FrontLineWorker> invalidFrontLineWorkerList() {
        return frontLineWorkerRecordDataService.executeQuery(new CustomQueries.DeleteFrontLineWorkerQuery());

    }

    @Override
    public void deleteInvalidFrontLineWorkerRecords() {
        List<FrontLineWorker> frontLineWorkerListToBeDeleted = invalidFrontLineWorkerList();

        if(frontLineWorkerListToBeDeleted != null){
            for (FrontLineWorker frontLineWorker : frontLineWorkerListToBeDeleted) {
                frontLineWorkerService.deleteFrontLineWorker(frontLineWorker);
            }
        }
        else {
            logger.debug("No invalid record found which is older than the purgeDate");
        }
    }
}
