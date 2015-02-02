package org.motechproject.nms.flw.service;

import org.motechproject.nms.flw.domain.FrontLineWorker;
import org.motechproject.nms.flw.domain.FrontLineWorkerCsv;

import java.util.List;

/**
 * Created by abhishek on 2/2/15.
 */
public interface FlwUploadRecordService {

    public void add(FrontLineWorker frontLineWorker);

    public List<FrontLineWorkerCsv> retrieveAllRecords();
}
