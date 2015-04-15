package org.motechproject.nms.mobileacademy.service;

import org.motechproject.nms.mobileacademy.domain.CallDetail;
import org.motechproject.nms.mobileacademy.domain.ContentLog;

public interface CallDetailService {

    void saveCallDetailRecord(CallDetail callDetail);

    void saveContentLogRecord(ContentLog contentLog);

}
