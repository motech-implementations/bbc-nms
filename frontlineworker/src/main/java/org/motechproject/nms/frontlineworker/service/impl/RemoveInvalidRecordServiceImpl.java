package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.service.RemoveInvalidRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 27/3/15.
 */
@Service("removeInvalidRecordService")
public class RemoveInvalidRecordServiceImpl implements RemoveInvalidRecordService {


    @Autowired
    private JobScheduler jobScheduler;


    @Override
    public void createNewDeletionJob() {

        jobScheduler.scheduleDeletionJob();

    }
}
