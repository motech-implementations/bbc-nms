//package org.motechproject.nms.kilkari.web;
//
//
//
//import org.motechproject.nms.util.helper.DataValidationException;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MissingServletRequestParameterException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//
//@ControllerAdvice
//public class BaseController extends ResponseEntityExceptionHandler {
//    /*
//     * (non-Javadoc)
//     *
//     * @see org.springframework.web.servlet.mvc.method.annotation.
//     * ResponseEntityExceptionHandler
//     * #handleMissingServletRequestParameter(org.springframework
//     * .web.bind.MissingServletRequestParameterException,
//     * org.springframework.http.HttpHeaders,
//     * org.springframework.http.HttpStatus,
//     * org.springframework.web.context.request.WebRequest)
//     */
//    @Override
//    protected ResponseEntity<Object> handleMissingServletRequestParameter(
//            MissingServletRequestParameterException ex, HttpHeaders headers,
//            HttpStatus status, WebRequest request) {
//        String responseJson = "{\"failureReason\":\"" + ex.getParameterName()
//                + ":Not Present\"}";
//        return new ResponseEntity<Object>(responseJson, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * Handle custom data validation exception i.e. not numeric ,not in range
//     * (400)
//     *
//     * @param ex
//     * @param req
//     * @return ResponseEntity<String>
//     */
//    @ExceptionHandler(value = { DataValidationException.class })
//    public ResponseEntity<String> handleDataValidationException(
//            final DataValidationException ex, final WebRequest req) {
//        String responseJson = "{\"failureReason\":\"" + ex.getErroneousField()
//                + ":Invalid Value\"}";
//        return new ResponseEntity<String>(responseJson, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * Handle General Exceptions occur on server side i.e null pointer(500)
//     *
//     * @param ex
//     * @param req
//     * @return ResponseEntity<String>
//     */
//    @ExceptionHandler(value = { Exception.class })
//    public ResponseEntity<String> handleGeneralExceptions(final Exception ex,
//                                                          final WebRequest req) {
//        String responseJson = "{\"failureReason\":\"Internal Error\"}";
//        return new ResponseEntity<String>(responseJson,
//                HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}