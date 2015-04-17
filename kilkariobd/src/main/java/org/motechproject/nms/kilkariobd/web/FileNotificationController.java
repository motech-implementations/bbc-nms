package org.motechproject.nms.kilkariobd.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FileNotificationController {
    Logger logger = LoggerFactory.getLogger(FileNotificationController.class);

    @RequestMapping(value = "/cdrFileNotification", method = RequestMethod.POST)
    public void CDRFileNotification() {

    }
}
