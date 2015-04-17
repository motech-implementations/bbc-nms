package org.motechproject.nms.kilkari.ut.dto;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.dto.request.SubscriptionDeactivateApiRequest;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;

public class SubscriptionDeactivateApiRequestTest {

    SubscriptionDeactivateApiRequest apiRequest;

    @Test
    public void shouldSetValuesInSubscriptionDeactivateApiRequest() {

        apiRequest = createDeactivateApiRequest();

        Assert.assertEquals("operator", apiRequest.getOperator());
        Assert.assertEquals("circle", apiRequest.getCircle());
        Assert.assertEquals("1234567890", apiRequest.getCalledNumber());
        Assert.assertEquals("callId", apiRequest.getCallId());
        Assert.assertTrue(1L == apiRequest.getSubscriptionId());
    }

    @Test
    public void shouldThrowDataValidationExceptionWhenSubscriptionIdIsNull() {

        apiRequest = createDeactivateApiRequest();
        apiRequest.setSubscriptionId(null);

        try {
            apiRequest.validateMandatoryParameter();
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) ex).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }
    }

    public SubscriptionDeactivateApiRequest createDeactivateApiRequest() {

        SubscriptionDeactivateApiRequest deactivateApiRequest = new SubscriptionDeactivateApiRequest();
        deactivateApiRequest.setOperator("operator");
        deactivateApiRequest.setCircle("circle");
        deactivateApiRequest.setCalledNumber("1234567890");
        deactivateApiRequest.setCallId("callId");
        deactivateApiRequest.setSubscriptionId(1L);

        return deactivateApiRequest;
    }
}
