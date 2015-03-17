package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.repository.FlwRecordDataService;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 13/3/15.
 */
@Service("frontLineWorkerService")
public class FrontLineWorkerServiceImpl implements FrontLineWorkerService {

    @Autowired
    FlwRecordDataService flwRecordDataService;

    @Override
    public void createFrontLineWorker(FrontLineWorker frontLineWorker) {
        flwRecordDataService.create(frontLineWorker);
    }

    @Override
    public void updateFrontLineWorker(FrontLineWorker frontLineWorker) {
        flwRecordDataService.update(frontLineWorker);
    }

    @Override
    public FrontLineWorker getFlwBycontactNo(String contactNo) {
        return flwRecordDataService.getFlwByContactNo(contactNo);
    }

    @Override
    public FrontLineWorker getFlwByFlwIdAndStateId(Long flwId, Long stateCode) {
        return flwRecordDataService.getFlwByFlwIdAndStateId(flwId, stateCode);
    }
}
