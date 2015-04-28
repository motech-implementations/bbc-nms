package org.motechproject.nms.kilkariobd.ut.dto;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.builder.RequestBuilder;
import org.motechproject.nms.kilkariobd.commons.Constants;
import org.motechproject.nms.kilkariobd.dto.request.CdrInfo;
import org.motechproject.nms.kilkariobd.dto.request.CdrNotificationRequest;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.constants.ErrorDescriptionConstants;
import org.motechproject.nms.util.helper.DataValidationException;

public class CdrNotificationRequestTest {

    @Test
    public void validateMandatoryParametersWithCDRDetailNull() {
        RequestBuilder requestBuilder = new RequestBuilder();
        CdrInfo cdrSummary = requestBuilder.buildCdrInfo("file", "checksum", 1L);
        CdrNotificationRequest cdrNotificationRequest =
                requestBuilder.buildCdrNotificationRequest("filename", cdrSummary, null);
        try {
            cdrNotificationRequest.validateMandatoryParameters();
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) ex).getErrorCode(),ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING );
            Assert.assertEquals(((DataValidationException)ex).getErrorDesc(), String.format(ErrorDescriptionConstants.MISSING_API_PARAMETER_DESCRIPTION, Constants.CDR_DETAIL_INFO));

        }
    }

    @Test
    public void validateMandatoryParametersWithCDRDetailRecordCountNull() {
        RequestBuilder requestBuilder = new RequestBuilder();
        CdrInfo cdrSummary = requestBuilder.buildCdrInfo("file", "checksum", 1L);
        CdrInfo cdrDetail = requestBuilder.buildCdrInfo("cdrFile", "cdrChecksum", null);
        CdrNotificationRequest cdrNotificationRequest =
                requestBuilder.buildCdrNotificationRequest("filename", cdrSummary, cdrDetail);

        try {
            cdrNotificationRequest.validateMandatoryParameters();
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) ex).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
            Assert.assertEquals(((DataValidationException)ex).getErrorDesc(),  String.format(ErrorDescriptionConstants.INVALID_API_PARAMETER_DESCRIPTION, Constants.CDR_DETAIL_RECORDS_COUNT));
        }
    }

    @Test
    public void validateMandatoryParametersWithCDRSummaryNull() {
        RequestBuilder requestBuilder = new RequestBuilder();
        CdrInfo cdrDetail = requestBuilder.buildCdrInfo("cdrFile", "cdrChecksum", 1L);
        CdrNotificationRequest cdrNotificationRequest =
                requestBuilder.buildCdrNotificationRequest("filename", null, cdrDetail);

        cdrNotificationRequest.setFileName("filename");
        cdrNotificationRequest.setCdrDetail(cdrDetail);
        cdrNotificationRequest.setCdrSummary(null);

        try {
            cdrNotificationRequest.validateMandatoryParameters();
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) ex).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
            Assert.assertEquals(((DataValidationException)ex).getErrorDesc(), String.format(ErrorDescriptionConstants.MISSING_API_PARAMETER_DESCRIPTION, Constants.CDR_SUMMARY_INFO));
        }
    }

    @Test
    public void validateMandatoryParametersWithCDRSummaryRecordCountNull() {
        RequestBuilder requestBuilder = new RequestBuilder();
        CdrInfo cdrSummary = requestBuilder.buildCdrInfo("file", "checksum", null);
        CdrInfo cdrDetail = requestBuilder.buildCdrInfo("cdrFile", "cdrChecksum", 1L);
        CdrNotificationRequest cdrNotificationRequest =
                requestBuilder.buildCdrNotificationRequest("filename", cdrSummary, cdrDetail);

        try {
            cdrNotificationRequest.validateMandatoryParameters();
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) ex).getErrorCode(), ErrorCategoryConstants.INVALID_DATA);
            Assert.assertEquals(((DataValidationException)ex).getErrorDesc(), String.format(ErrorDescriptionConstants.INVALID_API_PARAMETER_DESCRIPTION, Constants.CDR_SUMMARY_RECORDS_COUNT));
        }
    }
}
