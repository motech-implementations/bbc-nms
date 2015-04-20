package org.motechproject.nms.frontlineworker.service.impl;

import org.motechproject.nms.frontlineworker.service.RemoveInvalidRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * The purpose of this class is to perform initialization for MobileKunji Service.
 * It creates and initializes the configuration parameters with default values, if not created already
 */
@Component
public class Initializer {

    private static final Logger LOG = LoggerFactory.getLogger(Initializer.class);

    private RemoveInvalidRecordService removeInvalidRecordService;

    public Initializer() {
    }

    @Autowired
    public Initializer(RemoveInvalidRecordService removeInvalidRecordService) {
        this.removeInvalidRecordService = removeInvalidRecordService;
    }

    @PostConstruct
    public void initializeConfiguration() {

        removeInvalidRecordService.createNewDeletionJob();
    }

}
