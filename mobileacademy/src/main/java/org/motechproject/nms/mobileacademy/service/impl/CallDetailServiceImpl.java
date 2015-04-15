package org.motechproject.nms.mobileacademy.service.impl;

import org.motechproject.nms.mobileacademy.domain.CallDetail;
import org.motechproject.nms.mobileacademy.domain.ContentLog;
import org.motechproject.nms.mobileacademy.repository.CallDetailDataService;
import org.motechproject.nms.mobileacademy.repository.ContentLogDataService;
import org.motechproject.nms.mobileacademy.service.CallDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CallDetailService")
public class CallDetailServiceImpl implements CallDetailService {

    @Autowired
    private CallDetailDataService callDetailDataService;

    @Autowired
    private ContentLogDataService contentLogDataService;

    @Override
    public void saveCallDetailRecord(CallDetail callDetail) {
        callDetailDataService.create(callDetail);
    }

    @Override
    public void saveContentLogRecord(ContentLog contentLog) {
        contentLogDataService.create(contentLog);
    }

}
