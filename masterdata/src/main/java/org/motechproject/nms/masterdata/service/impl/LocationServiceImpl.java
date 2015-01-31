package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.*;
import org.motechproject.nms.masterdata.repository.DistrictRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.motechproject.nms.masterdata.repository.TalukaRecordsDataService;
import org.motechproject.nms.masterdata.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("locationService")
public class LocationServiceImpl implements LocationService {

    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    private DistrictRecordsDataService districtRecordsDataService;


    @Autowired
    private TalukaRecordsDataService talukaRecordsDataService;

    @Override
    public boolean validateLocation(Long stateId, Long districtId) {
        return true;
    }

    /**
     * get State record for given State Census code
     *
     * @param stateCode State Census Code
     * @return State object corresponding to the census code
     */
    @Override
    public State getStateByCode(Long stateCode) {

        return stateRecordsDataService.findRecordByStateCode(stateCode);
    }

    /**
     * get District record for given District Census code
     *
     * @param stateId      MDS Generated id of the state record in which to look up for district
     * @param districtCode District Census Code
     * @return District object corresponding to the Census code
     */
    @Override
    public District getDistrictByCode(Long stateId, Long districtCode) {

        return null;
    }

    /**
     * get Taluka record for given Taluka Census (or Proposed Census) Code
     *
     * @param districtId MDS Generated id of the District record, in which to look up for Taluka
     * @param talukaCode Taluka Census (or Proposed Census) Code
     * @return Taluka object corresponding to the Census (or Proposed Census)code
     */
    @Override
    public Taluka getTalukaByCode(Long districtId, String talukaCode) {
        return null;
    }

    /**
     * get HealthBlock record for given HealthBlock Census (or Proposed Census) Code
     *
     * @param talukaId        MDS Generated id of the Taluka record, in which to look up for HealthBlock
     * @param healthBlockCode HealthBlock Census (or Proposed Census) Code
     * @return HealthBlock object corresponding to the Census (or Proposed Census)code
     */
    @Override
    public HealthBlock getHealthBlockByCode(Long talukaId, Long healthBlockCode) {
        return null;
    }

    /**
     * get HealthFacility record for given HealthFacility Census (or Proposed Census) Code
     *
     * @param healthBlockId      MDS Generated id of the HealthBlock record, in which to look up for HealthFacility
     * @param healthFacilityCode HealthFacility Census (or Proposed Census) Code
     * @return HealthFacility object corresponding to the Census (or Proposed Census)code
     */
    @Override
    public HealthFacility getHealthFacilityByCode(Long healthBlockId, Long healthFacilityCode) {
        return null;
    }
    /**
     * get HealthSubFacility record for given HealthSubFacility Census (or Proposed Census) Code
     *
     * @param healthFacilityId      MDS Generated id of the HealthFacility record, in which to look up for HealthSubFacility
     * @param healthSubFacilityCode HealthSubFacility Census (or Proposed Census) Code
     * @return HealthSubFacility object corresponding to the Census (or Proposed Census)code
     */
    @Override
    public HealthSubFacility getHealthSubFacilityByCode(Long healthFacilityId, Long healthSubFacilityCode) {
        return null;
    }
    /**
     * get Village record for given Village Census (or MCTS ) Code
     *
     * @param talukaId    MDS Generated id of the Taluka record, in which to look up for Village
     * @param villageCode Village Census (or MCTS ) Code
     * @return Village object corresponding to the Census (or MCTS )code
     */
    @Override
    public Village getVillageByCode(Long talukaId, Long villageCode) {
        return null;
    }
}
