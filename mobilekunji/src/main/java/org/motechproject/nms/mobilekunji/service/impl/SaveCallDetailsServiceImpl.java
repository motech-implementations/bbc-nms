package org.motechproject.nms.mobilekunji.service.impl;


import org.motechproject.nms.mobilekunji.service.SaveCallDetailsService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionCallService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionFlwService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 13/3/15.
 */

@Service("saveCallDetailsService")
public class SaveCallDetailsServiceImpl implements SaveCallDetailsService {

    private ServiceConsumptionCallService serviceConsumptionCallService;

    private ServiceConsumptionFlwService serviceConsumptionFlwService;

    private Logger logger = LoggerFactory.getLogger(SaveCallDetailsServiceImpl.class);

    @Autowired
    public SaveCallDetailsServiceImpl(ServiceConsumptionCallService serviceConsumptionCallService, ServiceConsumptionFlwService serviceConsumptionFlwService) {
        this.serviceConsumptionCallService = serviceConsumptionCallService;
        this.serviceConsumptionFlwService = serviceConsumptionFlwService;
    }

    @Override
    public void saveCallDetails() {

    }
}
