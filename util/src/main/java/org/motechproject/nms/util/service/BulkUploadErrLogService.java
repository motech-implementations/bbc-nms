package bulkuploadloggingutil;


public interface BulkUploadErrLogService {

    public void writeBulkUploadErrLog(String logFileName, String erroneousRecord, String errorCode, String errorCause);

    public void writeBulkUploadProcessingSummary(String logFileName, Integer numberOfSuccessfulRecords, Integer numberOfFailedRecords);
}
