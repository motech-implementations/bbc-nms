package org.motechproject.nms.kilkariobd.dto.request;

import org.codehaus.jackson.annotate.JsonProperty;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;

/**
 * Entity to represent the CdrNotification request object.
 */
public class CdrNotificationRequest {
    @JsonProperty
    String fileName;

    @JsonProperty
    CdrInfo cdrSummary;

    @JsonProperty
    CdrInfo cdrDetail;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public CdrInfo getCdrSummary() {
        return cdrSummary;
    }

    public void setCdrSummary(CdrInfo cdrSummary) {
        this.cdrSummary = cdrSummary;
    }

    public CdrInfo getCdrDetail() {
        return cdrDetail;
    }

    public void setCdrDetail(CdrInfo cdrDetail) {
        this.cdrDetail = cdrDetail;
    }

    /**
     * Method to validate the mandatory parameters of this class.
     * @throws DataValidationException is to be thrown if one of the mandatory parameter is missing
     */
    public void validateMandatoryParameters() throws DataValidationException{
        ParseDataHelper.validateAndParseString(Constants.FILE_NAME, fileName, true);

        if (cdrDetail == null) {
            ParseDataHelper.raiseMissingDataException(Constants.CDR_DETAIL_INFO, null);
        } else {
            ParseDataHelper.validateAndParseString(Constants.CDR_DETAIL_CHECKSUM, cdrDetail.getCdrChecksum(), true);
            ParseDataHelper.validateAndParseString(Constants.CDR_DETAIL_FILE, cdrDetail.getCdrFile(), true);
            if (cdrDetail.getRecordsCount() == null) {
                ParseDataHelper.raiseMissingDataException(Constants.CDR_DETAIL_RECORDS_COUNT, null);
            }
        }

        if (cdrSummary == null) {
            ParseDataHelper.raiseMissingDataException(Constants.CDR_SUMMARY_INFO, null);
        } else {

            if (cdrSummary.getRecordsCount() == null) {
                ParseDataHelper.raiseMissingDataException(Constants.CDR_SUMMARY_RECORDS_COUNT, null);
            }
            ParseDataHelper.validateAndParseString(Constants.CDR_SUMMARY_FILE, cdrSummary.getCdrFile(), true);
            ParseDataHelper.validateAndParseString(Constants.CDR_SUMMARY_CHECKSUM, cdrSummary.getCdrChecksum(), true);
        }
    }

}
