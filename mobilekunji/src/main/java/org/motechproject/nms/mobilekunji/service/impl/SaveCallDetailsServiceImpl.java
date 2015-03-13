package org.motechproject.nms.mobilekunji.service.impl;


import org.motechproject.nms.mobilekunji.repository.SaveCallDetailsRecordDataService;
import org.motechproject.nms.mobilekunji.service.SaveCallDetailsService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 13/3/15.
 */

@Service("saveCallDetailsService")
public class SaveCallDetailsServiceImpl implements SaveCallDetailsService {

    private SaveCallDetailsRecordDataService saveCallDetailsRecordDataService;

    private Logger logger = LoggerFactory.getLogger(SaveCallDetailsServiceImpl.class);

    @Autowired
    public SaveCallDetailsServiceImpl(SaveCallDetailsRecordDataService saveCallDetailsRecordDataService) {
        this.saveCallDetailsRecordDataService = saveCallDetailsRecordDataService;
    }

    @Override
    public void saveCallDetails() {

    }
}
