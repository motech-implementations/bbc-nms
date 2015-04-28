package org.motechproject.nms.kilkariobd.builder;

import org.motechproject.nms.kilkariobd.dto.request.CdrInfo;
import org.motechproject.nms.kilkariobd.dto.request.CdrNotificationRequest;

public class RequestBuilder {

    public CdrNotificationRequest buildCdrNotificationRequest(String fileName, CdrInfo cdrSummary, CdrInfo cdrDetail) {
        CdrNotificationRequest request = new CdrNotificationRequest();
        request.setFileName(fileName);
        request.setCdrDetail(cdrDetail);
        request.setCdrSummary(cdrSummary);
        return request;
    }

    public CdrInfo buildCdrInfo(String fileName, String checksum, Long recordCount) {
        CdrInfo cdrInfo = new CdrInfo();
        cdrInfo.setCdrChecksum(checksum);
        cdrInfo.setCdrFile(fileName);
        cdrInfo.setRecordsCount(recordCount);
        return cdrInfo;
    }


}

