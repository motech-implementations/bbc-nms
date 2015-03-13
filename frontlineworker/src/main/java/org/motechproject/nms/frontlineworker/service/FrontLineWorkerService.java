package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;

/**
 * Created by abhishek on 13/3/15.
 */
public interface FrontLineWorkerService {

    public void createFrontLineWorker(FrontLineWorker frontLineWorker);

    public void updateFrontLineWorker(FrontLineWorker frontLineWorker);

    public FrontLineWorker getFlwBycontactNo(String contactNo);

    public FrontLineWorker getFlwByFlwIdAndStateId(Long flwId, Long stateCode);

}
