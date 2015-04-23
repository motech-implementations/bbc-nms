package org.motechproject.nms.mobileacademy.web;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.motechproject.nms.frontlineworker.exception.FlwNotInWhiteListException;
import org.motechproject.nms.frontlineworker.exception.ServiceNotDeployedException;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
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

    private static final String FAILURE_REASON = "\"failureReason\"";

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
        String responseJson = "{" + FAILURE_REASON + ":\""
                + exception.getParameterName() + ":Not Present\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(responseJson, headers,
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle invalid HTTP request messages i.e. invalid JSON request
     *
     * @param exception
     * @param request
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    public ResponseEntity<String> handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException exception,
            final HttpServletRequest request) {
        logRequestDetails(request);
        LOGGER.error(exception.getMessage(), exception);
        String responseJson = "{" + FAILURE_REASON + ":\"Invalid JSON\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(responseJson, headers,
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle invalid HTTP content Type
     *
     * @param exception
     * @param request
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(value = { HttpMediaTypeNotSupportedException.class })
    public ResponseEntity<String> handleHttpMediaTypeNotSupportedException(
            final HttpMediaTypeNotSupportedException exception,
            final HttpServletRequest request) {
        logRequestDetails(request);
        LOGGER.error(exception.getMessage(), exception);
        String responseJson = "{" + FAILURE_REASON
                + ":\"Invalid Content Type\"}";
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
        String responseJson = "{" + FAILURE_REASON + ":\""
                + exception.getErroneousField() + ":Invalid Value\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(responseJson, headers,
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle Internal exception i.e. course not present or course upload is
     * ongoing or other business exceptions.
     *
     * @param exception
     * @param request
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(value = { NmsInternalServerError.class })
    public ResponseEntity<String> handleInternalException(
            final NmsInternalServerError exception,
            final HttpServletRequest request) {
        logRequestDetails(request);
        LOGGER.error(exception.getMessage(), exception);
        String responseJson = "{" + FAILURE_REASON + ":\""
                + exception.getMessage() + "\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(responseJson, headers,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle Service Not Deployed Exception(501)
     *
     * @param exception
     * @param request
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(value = { ServiceNotDeployedException.class })
    public ResponseEntity<String> handleServiceNotDeployedException(
            final ServiceNotDeployedException exception,
            final HttpServletRequest request) {
        logRequestDetails(request);
        LOGGER.error(exception.getMessage(), exception);
        String responseJson = "{" + FAILURE_REASON
                + ":\"Service Not Deployed\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(responseJson, headers,
                HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * Handle FLW Not In White List Exception(403)
     *
     * @param exception
     * @param request
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(value = { FlwNotInWhiteListException.class })
    public ResponseEntity<String> handleFlwNotInWhiteListException(
            final FlwNotInWhiteListException exception,
            final HttpServletRequest request) {
        logRequestDetails(request);
        LOGGER.error(exception.getMessage(), exception);
        String responseJson = "{" + FAILURE_REASON
                + ":\"User is not Present in WhiteList\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(responseJson, headers,
                HttpStatus.FORBIDDEN);
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
        String responseJson = "{" + FAILURE_REASON + ":\"Internal Error\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<String>(responseJson, headers,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * if parameter is missing i.e value is null then throw
     * MissingServletRequestParameterException
     * 
     * @param parameterName name of the parameter
     * @param parameterValue value of the parameter
     * @throws MissingServletRequestParameterException
     */
    public void checkParameterMissing(String parameterName,
            String parameterValue)
            throws MissingServletRequestParameterException {
        if (parameterValue == null) {
            throw new MissingServletRequestParameterException(parameterName,
                    null);
        }
    }

    /**
     * Log incoming Request Details in case exception occur.
     * 
     * @param request
     */
    private void logRequestDetails(final HttpServletRequest request) {
        try {
            StringBuilder details = new StringBuilder("Request Details:\n");
            details.append("URL: " + request.getRequestURL() + "\n");
            details.append("Method Type: " + request.getMethod() + "  ");
            details.append("Content Type: " + request.getContentType() + "\n");
            // Log request parameters for get request
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                details.append("Query Parameters:\n");
                Enumeration<?> params = request.getParameterNames();
                while (params.hasMoreElements()) {
                    String paramName = (String) params.nextElement();
                    details.append(paramName + ":"
                            + request.getParameter(paramName) + " ");
                }
                details.append("\n");
            }
            LOGGER.error(details.toString());
        } catch (Exception e) {
            LOGGER.error(
                    "Error occured while finding Input Request details for logging",
                    e);
        }
    }

}
