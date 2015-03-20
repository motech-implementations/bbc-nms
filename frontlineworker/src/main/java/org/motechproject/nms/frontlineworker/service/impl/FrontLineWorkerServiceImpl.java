package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.repository.FrontLineWorkerRecordDataService;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class acts as implementation class for the interface FrontLineWorkerService.
 * It uses FrontLineWorkerRecordDataService which further takes MotechDataService as
 * base class to performs the CRUD operations on FrontLineWorker records. It also adds lookup
 * procedures to fetch the frontLineWorker record from Database.
 */
@Service("frontLineWorkerService")
public class FrontLineWorkerServiceImpl implements FrontLineWorkerService {


    public FrontLineWorkerRecordDataService frontLineWorkerRecordDataService;

    @Autowired
    public FrontLineWorkerServiceImpl(FrontLineWorkerRecordDataService frontLineWorkerRecordDataService) {
        this.frontLineWorkerRecordDataService = frontLineWorkerRecordDataService;
    }

    @Override
    public void createFrontLineWorker(FrontLineWorker frontLineWorker) {
        frontLineWorkerRecordDataService.create(frontLineWorker);
    }

    @Override
    public void updateFrontLineWorker(FrontLineWorker frontLineWorker) {
        frontLineWorkerRecordDataService.update(frontLineWorker);
    }

    @Override
    public FrontLineWorker getFlwBycontactNo(String contactNo) {
        return frontLineWorkerRecordDataService.getFlwByContactNo(contactNo);
    }

    @Override
    public FrontLineWorker getFlwByFlwIdAndStateId(Long flwId, Long stateCode) {
        return frontLineWorkerRecordDataService.getFlwByFlwIdAndStateId(flwId, stateCode);
    }

    @Override
    public FrontLineWorker findById(Long id) {
        return frontLineWorkerRecordDataService.findById(id);
    }
}
