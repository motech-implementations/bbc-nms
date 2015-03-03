package org.motechproject.nms.mobilekunji.ut.web;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.nms.mobilekunji.web.BaseController;
import org.motechproject.nms.util.helper.DataValidationException;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * This class is used to test BaseController.
 */
public class BaseControllerTest extends TestCase {

    private BaseController baseController;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private DataValidationException dataValidationException;


    @Before
    public void setUp() {
        initMocks(this);
        this.baseController = new BaseController();
    }

    @Test
    public void testHandleDataValidationException() {
        
        when(httpServletRequest.getRequestURL()).thenReturn(new StringBuffer("anyUrl"));
        when(httpServletRequest.getMethod()).thenReturn(new String("anyMethod"));
        when(httpServletRequest.getContentType()).thenReturn(new String("anyContent"));
        when(httpServletRequest.getMethod()).thenReturn(new String("GET"));




        baseController.handleDataValidationException(dataValidationException, httpServletRequest);
    }
}
