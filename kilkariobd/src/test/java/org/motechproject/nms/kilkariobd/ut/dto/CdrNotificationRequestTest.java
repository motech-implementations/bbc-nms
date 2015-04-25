package org.motechproject.nms.kilkariobd.ut.dto;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.dto.request.CdrInfo;
import org.motechproject.nms.kilkariobd.dto.request.CdrNotificationRequest;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;

public class CdrNotificationRequestTest {

    @Test
    public void validateMandatoryParametersWithCDRDetailNull() {

        CdrNotificationRequest cdrNotificationRequest = new CdrNotificationRequest();

        CdrInfo cdrSummary = new CdrInfo();
        cdrSummary.setCdrChecksum("checksum");
        cdrSummary.setCdrFile("file");
        cdrSummary.setRecordsCount(1L);

        cdrNotificationRequest.setFileName("filename");
        cdrNotificationRequest.setCdrDetail(null);
        cdrNotificationRequest.setCdrSummary(cdrSummary);

        try {
            cdrNotificationRequest.validateMandatoryParameters();
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) ex).getErrorCode(),ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING );
        }
    }

    @Test
    public void validateMandatoryParametersWithCDRDetailRecordCountNull() {

        CdrNotificationRequest cdrNotificationRequest = new CdrNotificationRequest();

        CdrInfo cdrSummary = new CdrInfo();
        CdrInfo cdrDetail = new CdrInfo();

        cdrSummary.setCdrChecksum("checksum");
        cdrSummary.setCdrFile("file");
        cdrSummary.setRecordsCount(1L);

        cdrDetail.setRecordsCount(null);
        cdrDetail.setCdrFile("cdrFile");
        cdrDetail.setCdrChecksum("cdrChecksum");

        cdrNotificationRequest.setFileName("filename");
        cdrNotificationRequest.setCdrDetail(cdrDetail);
        cdrNotificationRequest.setCdrSummary(cdrSummary);

        try {
            cdrNotificationRequest.validateMandatoryParameters();
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) ex).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }
    }

    @Test
    public void validateMandatoryParametersWithCDRSummaryNull() {

        CdrNotificationRequest cdrNotificationRequest = new CdrNotificationRequest();

        CdrInfo cdrDetail = new CdrInfo();

        cdrDetail.setRecordsCount(1L);
        cdrDetail.setCdrFile("cdrFile");
        cdrDetail.setCdrChecksum("cdrChecksum");

        cdrNotificationRequest.setFileName("filename");
        cdrNotificationRequest.setCdrDetail(cdrDetail);
        cdrNotificationRequest.setCdrSummary(null);

        try {
            cdrNotificationRequest.validateMandatoryParameters();
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) ex).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }
    }

    @Test
    public void validateMandatoryParametersWithCDRSummaryRecordCountNull() {

        CdrNotificationRequest cdrNotificationRequest = new CdrNotificationRequest();

        CdrInfo cdrSummary = new CdrInfo();
        CdrInfo cdrDetail = new CdrInfo();

        cdrSummary.setCdrChecksum("checksum");
        cdrSummary.setCdrFile("file");
        cdrSummary.setRecordsCount(null);

        cdrDetail.setRecordsCount(1L);
        cdrDetail.setCdrFile("cdrFile");
        cdrDetail.setCdrChecksum("cdrChecksum");

        cdrNotificationRequest.setFileName("filename");
        cdrNotificationRequest.setCdrDetail(cdrDetail);
        cdrNotificationRequest.setCdrSummary(cdrSummary);

        try {
            cdrNotificationRequest.validateMandatoryParameters();
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) ex).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }
    }



}
