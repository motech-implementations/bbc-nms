package org.motechproject.nms.frontlineworker.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.frontlineworker.Status;
import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by abhishek on 27/3/15.
 */
@Component
public class RemoveInvalidRecordHandler {

    private FrontLineWorkerService frontLineWorkerService;

    @Autowired
    public RemoveInvalidRecordHandler(FrontLineWorkerService frontLineWorkerService) {
        this.frontLineWorkerService = frontLineWorkerService;
    }

    @MotechListener(subjects = {ConfigurationConstants.DELETION_EVENT_SUBJECT_SCHEDULER})
    public void handleDeleteInvalidFlw(MotechEvent motechEvent) {


        List<FrontLineWorker> frontLineWorkerList = frontLineWorkerService.retrieveAll();

        for (FrontLineWorker frontLineWorker : frontLineWorkerList) {
            if (frontLineWorker.getStatus() == Status.INVALID) {
                frontLineWorkerService.deleteFrontLineWorker(frontLineWorker);
            }
        }

        // scheduleRepeatDeletion(motechEvent);
    }

    private void scheduleRepeatDeletion(MotechEvent motechEvent) {

    }


}
