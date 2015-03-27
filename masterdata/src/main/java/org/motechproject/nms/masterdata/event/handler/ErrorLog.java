package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.service.BulkUploadErrLogService;

/**
 * This class is used to Log the errors.
 */
public class ErrorLog {

    static void errorLog(BulkUploadError errorDetail,BulkUploadStatus uploadStatus,BulkUploadErrLogService bulkUploadErrLogService,String errorDescription,String errorCategory,String recordDetails)
    {
        errorDetail.setErrorDescription(errorDescription);
        errorDetail.setErrorCategory(errorCategory);
        errorDetail.setRecordDetails(recordDetails);
        bulkUploadErrLogService.writeBulkUploadErrLog(errorDetail);
        uploadStatus.incrementFailureCount();
    }
}
