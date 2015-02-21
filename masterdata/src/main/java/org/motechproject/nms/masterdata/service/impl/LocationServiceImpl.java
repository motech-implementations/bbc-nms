package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.*;
import org.motechproject.nms.masterdata.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("locationService")
public class LocationServiceImpl implements LocationService {

    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    private DistrictRecordsDataService districtRecordsDataService;

    @Override
    public boolean validateLocation(Long stateId, Long districtId) {
        return true;
    }

    @Override
    public State getStateByCode(Long stateCode) {
        return stateRecordsDataService.findRecordByStateCode(stateCode);
    }

    public District getDistrictByCode(Long stateId, Long districtCode) {
        return districtRecordsDataService.findRecordByDistrictCode(districtCode);
    }

    public Taluka getTalukaByCode(Long districtId, String talukaCode) {
        return null;
    }

    public HealthBlock getHealthBlockByCode(Long talukaId, Long healthBlockCode) {
        return null;
    }

    public HealthFacility getHealthFacilityByCode(Long healthBlockId, Long healthFacilityCode) {
        return null;
    }

    public HealthSubFacility getHealthSubFacilityByCode(Long healthFacilityId, Long healthSubFacilityCode) {
        return null;
    }

    public Village getVillageByCode(Long talukaId, Long villageCode) {
        return null;
    }
}
