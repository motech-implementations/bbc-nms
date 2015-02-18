package org.motechproject.nms.mobilekunji.web;


import org.motechproject.nms.mobilekunji.service.MobileKunjiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for HelloWorld message and bundle status.
 */
@Controller
public class MobileKunjiController {

    @Autowired
    private MobileKunjiService helloWorldService;

    private static final String OK = "OK";

    @RequestMapping("/web-api/status")
    @ResponseBody
    public String status() {
        return OK;
    }

    @RequestMapping("/sayHello")
    @ResponseBody
    public String sayHello() {
        return String.format("{\"message\":\"%s\"}", helloWorldService.sayHello());
    }
}
