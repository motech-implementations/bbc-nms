package org.motechproject.nms.kilkari.ut.dto;


import org.junit.Assert;
import org.junit.Test;
import org.motechproject.nms.kilkari.dto.request.SubscriptionDeactivateApiRequest;
import org.motechproject.nms.util.constants.ErrorCategoryConstants;
import org.motechproject.nms.util.helper.DataValidationException;

public class SubscriptionDeactivateApiRequestTest {



    @Test
    public void shouldSetValuesInSubscriptionDeactivateApiRequest() {

        SubscriptionDeactivateApiRequest apiRequest;

        apiRequest = createDeactivateApiRequest();

        try {
            apiRequest.validateMandatoryParameter();
        } catch (DataValidationException ex) {
            Assert.assertTrue(ex instanceof DataValidationException);
            Assert.assertEquals(((DataValidationException) ex).getErrorCode(), ErrorCategoryConstants.MANDATORY_PARAMETER_MISSING);
        }

        Assert.assertEquals("operator", apiRequest.getOperator());
        Assert.assertEquals("circle", apiRequest.getCircle());
        Assert.assertEquals("1234567890", apiRequest.getCalledNumber());
        Assert.assertEquals("123456", apiRequest.getCallId());
        Assert.assertTrue("1" == apiRequest.getSubscriptionId());
        Assert.assertTrue(1L == apiRequest.getSubscriptionIdLongValue());
    }

    @Test
    public void shouldThrowDataValidationExceptionWhenSubscriptionIdIsNull() {

        SubscriptionDeactivateApiRequest apiRequest;

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
        deactivateApiRequest.setCallId("123456");
        deactivateApiRequest.setSubscriptionId("1");

        return deactivateApiRequest;
    }
}
