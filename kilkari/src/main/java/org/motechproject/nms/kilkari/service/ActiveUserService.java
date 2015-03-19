package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ActiveUser;
import org.motechproject.nms.kilkari.domain.Channel;
import org.motechproject.nms.kilkari.domain.Subscriber;
import org.motechproject.nms.kilkari.domain.Subscription;
import org.motechproject.nms.masterdata.domain.Operator;
import org.motechproject.nms.util.helper.DataValidationException;

import java.util.List;

public interface ActiveUserService {

    ActiveUser create(ActiveUser activeUser);

    ActiveUser getActiveUserCount();
    
}
