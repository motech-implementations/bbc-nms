package org.motechproject.nms.kilkari.web;

import org.apache.log4j.Logger;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.NmsInternalServerError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

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
            final WebRequest request) {
        LOGGER.error(exception.getMessage(), exception);
        String responseJson = "{\"failureReason\":\""
                + exception.getParameterName() + ":Not Present\"}";
        return new ResponseEntity<String>(responseJson, HttpStatus.BAD_REQUEST);
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
            final DataValidationException exception, final WebRequest request) {
        LOGGER.error(exception.getMessage(), exception);
        String responseJson = "{\"failureReason\":\""
                + exception.getErroneousField() + ":Invalid Value\"}";
        return new ResponseEntity<String>(responseJson, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle General Exceptions occur on server side i.e null pointer(500)
     *
     * @param exception
     * @param request
     * @return ResponseEntity<String>
     */
    @ExceptionHandler(value = { NmsInternalServerError.class, Exception.class })
    public ResponseEntity<String> handleGeneralExceptions(
            final Exception exception, final WebRequest request) {

        LOGGER.error(exception.getMessage(), exception);
        String responseJson = "{\"failureReason\":\"" + exception.getMessage() +"}";
        return new ResponseEntity<String>(responseJson,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
