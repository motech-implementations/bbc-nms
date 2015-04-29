package org.motechproject.nms.kilkariobd.ut.dto;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkariobd.domain.FileProcessingStatus;
import org.motechproject.nms.kilkariobd.dto.request.FileProcessedStatusRequest;
import org.motechproject.nms.util.helper.DataValidationException;

public class FileProcessedStatusRequestTest {

    @Test
    public void shouldValidateMandatoryParams() {

        FileProcessedStatusRequest fileProcessedStatusRequest = new FileProcessedStatusRequest("8000","filename",null,null);
        try {
            fileProcessedStatusRequest.validateMandatoryParams();
            Assert.assertEquals(FileProcessingStatus.FILE_PROCESSED_SUCCESSFULLY, fileProcessedStatusRequest.getStatus());

        } catch (DataValidationException ex) {
        }
    }
}
