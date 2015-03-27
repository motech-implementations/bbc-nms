package org.motechproject.nms.masterdata.event.handler;

import org.joda.time.DateTime;
import org.motechproject.nms.util.domain.BulkUploadError;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.domain.RecordType;
import org.motechproject.nms.util.service.BulkUploadErrLogService;

/**
 * This class is used to  Create Log the errors.
 */
public class ErrorLog {

    /**
     * Saves the Error Logs and Updates the uploadStatus
     * @param errorDetail
     * @param uploadStatus
     * @param bulkUploadErrLogService
     * @param errorDescription
     * @param errorCategory
     * @param recordDetails
     */
    static void errorLog(BulkUploadError errorDetail,BulkUploadStatus uploadStatus,BulkUploadErrLogService bulkUploadErrLogService,String errorDescription,String errorCategory,String recordDetails)
    {
        errorDetail.setErrorDescription(errorDescription);
        errorDetail.setErrorCategory(errorCategory);
        errorDetail.setRecordDetails(recordDetails);
        bulkUploadErrLogService.writeBulkUploadErrLog(errorDetail);
        uploadStatus.incrementFailureCount();
    }

    /**
     * Sets the values for error log and upload status
     * @param errorDetail
     * @param uploadStatus
     * @param csvFileName
     * @param timeStamp
     * @param recordType
     */
    static void setErrorDetails(BulkUploadError errorDetail,BulkUploadStatus uploadStatus,String csvFileName,DateTime timeStamp,RecordType recordType)
    {
        errorDetail.setCsvName(csvFileName);
        errorDetail.setTimeOfUpload(timeStamp);
        errorDetail.setRecordType(recordType);

        uploadStatus.setBulkUploadFileName(csvFileName);
        uploadStatus.setTimeOfUpload(timeStamp);
    }
}
