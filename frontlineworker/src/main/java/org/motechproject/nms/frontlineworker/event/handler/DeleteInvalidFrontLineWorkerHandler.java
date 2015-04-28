package org.motechproject.nms.frontlineworker.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.frontlineworker.constants.ConfigurationConstants;
import org.motechproject.nms.frontlineworker.service.RemoveInvalidRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class provides listeners for deletion of invalid FrontLineWorkers
 */
@Component
public class DeleteInvalidFrontLineWorkerHandler {

    private RemoveInvalidRecords removeInvalidRecords;

    @Autowired
    public DeleteInvalidFrontLineWorkerHandler(RemoveInvalidRecords removeInvalidRecords) {
        this.removeInvalidRecords = removeInvalidRecords;
    }

    @MotechListener(subjects = {ConfigurationConstants.FLW_DELETE_EVENT_SUBJECT})
    public void flwDeletionHandler(MotechEvent motechEvent) {

        removeInvalidRecords.deleteInvalidFrontLineWorkerRecords();


    }

}
