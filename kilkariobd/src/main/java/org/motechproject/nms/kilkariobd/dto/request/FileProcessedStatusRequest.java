package org.motechproject.nms.kilkariobd.dto.request;

import com.google.gson.annotations.Expose;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * Entity represents the file processed status.
 */
public class FileProcessedStatusRequest {

    @Expose
    private String fileProcessingStatus;

    @Expose
    private String fileName;

    public FileProcessedStatusRequest(String status, String fileName) {
        this.fileProcessingStatus = status;
        this.fileName = fileName;
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
        Integer status = ParseDataHelper.validateInt(Constants.FILE_PROCESSING_STATUS, fileProcessingStatus, true);
        if (null == FileProcessingStatus.findBValue(status)) {
            ParseDataHelper.raiseApiParameterInvalidDataException(Constants.FILE_PROCESSING_STATUS, fileProcessingStatus);
        }

    }
}
