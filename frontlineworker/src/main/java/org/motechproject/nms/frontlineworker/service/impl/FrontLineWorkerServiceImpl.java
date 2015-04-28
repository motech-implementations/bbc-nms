package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;
import org.motechproject.nms.frontlineworker.enums.Status;
import org.motechproject.nms.frontlineworker.repository.FrontLineWorkerRecordDataService;
import org.motechproject.nms.frontlineworker.service.FrontLineWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void deleteFrontLineWorker(FrontLineWorker frontLineWorker) {
        frontLineWorkerRecordDataService.delete(frontLineWorker);
    }

    @Override
    public FrontLineWorker getFlwBycontactNo(String contactNo) {

        FrontLineWorker firstFrontLineWorker = null;
        FrontLineWorker validFrontLineWorker = null;
        List<FrontLineWorker> frontLineWorkerList = frontLineWorkerRecordDataService.getFlwByContactNo(contactNo);
        if (frontLineWorkerList.size() >= 1) {
            firstFrontLineWorker = frontLineWorkerList.get(0);
            for (FrontLineWorker frontLineWorker : frontLineWorkerList) {
                if (frontLineWorker.getStatus() != Status.INVALID) {
                    validFrontLineWorker = frontLineWorker;
                    break;
                }
            }

            if (validFrontLineWorker == null) {
                return firstFrontLineWorker;
            } else {
                return validFrontLineWorker;
            }
        }

        return validFrontLineWorker;

    }

    @Override
    public FrontLineWorker getFlwByFlwIdAndStateId(Long flwId, Long stateCode) {

        FrontLineWorker validFrontLineWorker = null;
        FrontLineWorker firstFrontLineWorker = null;

        List<FrontLineWorker> frontLineWorkerList = frontLineWorkerRecordDataService.getFlwByFlwIdAndStateId(flwId, stateCode);
        if (frontLineWorkerList.size() >= 1) {
            firstFrontLineWorker = frontLineWorkerList.get(0);
            for (FrontLineWorker frontLineWorker : frontLineWorkerList) {
                if (frontLineWorker.getStatus() != Status.INVALID) {
                    validFrontLineWorker = frontLineWorker;
                    break;
                }
            }

            if (validFrontLineWorker != null) {
                return validFrontLineWorker;
            } else {
                return firstFrontLineWorker;
            }
        }

        return validFrontLineWorker;

    }

    @Override
    public FrontLineWorker findById(Long id) {
        return frontLineWorkerRecordDataService.findById(id);
    }

    @Override
    public void deleteAll() {
        frontLineWorkerRecordDataService.deleteAll();
    }

}
