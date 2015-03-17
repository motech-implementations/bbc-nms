package org.motechproject.nms.frontlineworker.service;

import org.motechproject.nms.frontlineworker.domain.FrontLineWorkerCsv;

import java.util.List;

/**
 * Created by abhishek on 13/3/15.
 */
public interface FrontLineWorkerCsvService {


    public FrontLineWorkerCsv createFrontLineWorkerCsv(FrontLineWorkerCsv frontLineWorkerCsv);

    public FrontLineWorkerCsv findByIdInCsv(Long id);

    public void deleteFromCsv(FrontLineWorkerCsv frontLineWorkerCsv);

    public List<FrontLineWorkerCsv> retrieveAllFromCsv();
}
