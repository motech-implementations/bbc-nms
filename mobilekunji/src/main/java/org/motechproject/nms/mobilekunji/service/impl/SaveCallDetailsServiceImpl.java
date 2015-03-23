package org.motechproject.nms.mobilekunji.service.impl;


import org.motechproject.nms.mobilekunji.dto.SaveCallDetailApiRequest;
import org.motechproject.nms.mobilekunji.dto.SaveCallDetailApiResponse;
import org.motechproject.nms.mobilekunji.dto.UserDetailApiResponse;
import org.motechproject.nms.mobilekunji.service.SaveCallDetailsService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionCallService;
import org.motechproject.nms.mobilekunji.service.ServiceConsumptionFlwService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of SaveCallDetailsService
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
    public SaveCallDetailApiResponse saveCallDetails(UserDetailApiResponse userDetailApiResponse, SaveCallDetailApiRequest saveCallDetailApiRequest) {
        return null;
    }
}
