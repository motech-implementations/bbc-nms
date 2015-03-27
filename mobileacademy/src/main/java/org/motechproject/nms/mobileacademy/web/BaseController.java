package org.motechproject.nms.mobileacademy.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.motechproject.nms.util.helper.DataValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * BaseController class contain handlers for exceptions that occur in
 * controllers having rest api. Controller needs to extend this class to use
 * this functionality.
 *
 */
public class BaseController {

    private static final Logger LOGGER = Logger.getLogger(BaseController.class);

    /**
     * Handle Missing Servlet Request Parameters (400)
     *
     * @param exception
     * @param request
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(value = { MissingServletRequestParameterException.class })
    protected ResponseEntity<String> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException exception,
            final HttpServletRequest request) {
        logRequestDetails(request);
        LOGGER.error(exception.getMessage());
        String responseJson = "{\"failureReason\":\""
                + exception.getParameterName() + ":Not Present\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(responseJson, headers,
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle custom data validation exception i.e. not numeric ,not in range
     * (400)
     *
     * @param exception
     * @param request
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(value = { DataValidationException.class })
    public ResponseEntity<String> handleDataValidationException(
            final DataValidationException exception,
            final HttpServletRequest request) {
        logRequestDetails(request);
        LOGGER.error(exception.getMessage(), exception);
        String responseJson = "{\"failureReason\":\""
                + exception.getErroneousField() + ":Invalid Value\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(responseJson, headers,
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle General Exceptions occur on server side i.e null pointer(500)
     *
     * @param exception
     * @param request
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<String> handleGeneralExceptions(
            final Exception exception, final HttpServletRequest request) {
        logRequestDetails(request);
        LOGGER.error(exception.getMessage(), exception);
        String responseJson = "{\"failureReason\":\"Internal Error\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(responseJson, headers,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * handle exception if request JSON Parameter is missing
     * 
     * @param parameterName name of the parameter
     * @throws MissingServletRequestParameterException
     */
    public void handleMissingJsonParamException(String parameterName)
            throws MissingServletRequestParameterException {
        throw new MissingServletRequestParameterException(parameterName, null);
    }

    /**
     * Log incoming Request Details
     * 
     * @param request
     */
    private void logRequestDetails(final HttpServletRequest request) {
        StringBuilder details = new StringBuilder("Request Details:\n");
        details.append("URL: " + request.getRequestURL() + "\n");
        details.append("Content Type: " + request.getContentType() + "\n");
        details.append("Parameter Map: " + request.getParameterMap() + "\n");
        LOGGER.error(details.toString());
    }
}
