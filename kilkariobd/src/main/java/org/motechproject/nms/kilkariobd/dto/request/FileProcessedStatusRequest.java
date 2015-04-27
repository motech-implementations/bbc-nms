package org.motechproject.nms.kilkariobd.dto.request;

import com.google.gson.annotations.Expose;
import org.codehaus.jackson.annotate.JsonProperty;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * Entity represents the file processed status.
 */
public class FileProcessedStatusRequest {

    @JsonProperty
    private String fileProcessingStatus;

    @JsonProperty
    @Expose
    private String fileName;

    @Expose
    private FileProcessingStatus status;

    @Expose
    @JsonProperty
    private String failureReason;

    public FileProcessingStatus getStatus() {
        return status;
    }

    public void setStatus(FileProcessingStatus status) {
        this.status = status;
    }

    public FileProcessedStatusRequest(
            String fileProcessingStatus, String fileName, FileProcessingStatus status, String failureReason) {
        this.fileProcessingStatus = fileProcessingStatus;
        this.fileName = fileName;
        this.status = status;
        this.failureReason = failureReason;
    }

    public String getCdrFileProcessingStatus() {
        return fileProcessingStatus;
    }

    public void setCdrFileProcessingStatus(String cdrFileProcessingStatus) {
        fileProcessingStatus = cdrFileProcessingStatus;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void validateMandatoryParams() throws DataValidationException {
        ParseDataHelper.validateString(Constants.FILE_NAME, fileName, true);
        Integer statusValue = ParseDataHelper.validateInt(Constants.FILE_PROCESSING_STATUS, fileProcessingStatus, true);
        status = FileProcessingStatus.findBValue(statusValue);
        if (null == status) {
            ParseDataHelper.raiseApiParameterInvalidDataException(Constants.FILE_PROCESSING_STATUS, fileProcessingStatus);
        }

    }
}
