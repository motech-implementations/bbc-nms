package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.service.LocationService;
import org.springframework.stereotype.Service;

/**
 * Created by abhishek on 26/1/15.
 */
@Service("locationService")
public class LocationServiceImpl implements LocationService {


    @Override
    public boolean validateLocation(Long stateId, Long districtId) {
        return false;
    }
}
