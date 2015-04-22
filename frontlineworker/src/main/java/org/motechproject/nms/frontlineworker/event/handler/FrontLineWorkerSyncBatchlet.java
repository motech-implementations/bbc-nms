package org.motechproject.nms.frontlineworker.event.handler;

import org.springframework.stereotype.Service;

import javax.batch.api.Batchlet;

@Service
public class FrontLineWorkerSyncBatchlet implements Batchlet {

    @Override
    public String process() {
        System.out.println("inside process of FrontLineWorkerSyncBatchlet");

        return null;
    }

    @Override
    public void stop() {

    }

}
