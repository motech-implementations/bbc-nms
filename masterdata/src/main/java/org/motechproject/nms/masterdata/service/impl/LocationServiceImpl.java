package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.*;
import org.motechproject.nms.masterdata.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("locationService")
public class LocationServiceImpl implements LocationService {

    @Override
    public boolean validateLocation(Long stateId, Long districtId) {
        return true;
    }
}
