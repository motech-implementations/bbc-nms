package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.*;

/**
 * This interface is used for crud operations on Location Details
 */
public interface LocationService {

    /**
     * validates the Location by state Id and District Id
     * @param stateId
     * @param districtId
     * @return True/False
     */
    boolean validateLocation(Long stateId, Long districtId);


    /**
     * get State record for given State Census code
     *
     * @param stateCode State Census Code
     * @return State object corresponding to the census code
     */
    State getStateByCode(Long stateCode);

    /**
     * get District record for given District Census code
     *
     * @param stateId      MDS Generated id of the state record in which to look up for district
     * @param districtCode District Census Code
     * @return District object corresponding to the Census code
     */
    District getDistrictByCode(Long stateId, Long districtCode);

    /**
     * get Taluka record for given Taluka Census (or Proposed Census) Code
     *
     * @param districtId MDS Generated id of the District record, in which to look up for Taluka
     * @param talukaCode Taluka Census (or Proposed Census) Code
     * @return Taluka object corresponding to the Census (or Proposed Census)code
     */
    Taluka getTalukaByCode(Long districtId, Long talukaCode);

    /**
     * get HealthBlock record for given HealthBlock Census (or Proposed Census) Code
     *
     * @param talukaId        MDS Generated id of the Taluka record, in which to look up for HealthBlock
     * @param healthBlockCode HealthBlock Census (or Proposed Census) Code
     * @return HealthBlock object corresponding to the Census (or Proposed Census)code
     */
    HealthBlock getHealthBlockByCode(Long talukaId, Long healthBlockCode);

    /**
     * get HealthFacility record for given HealthFacility Census (or Proposed Census) Code
     *
     * @param healthBlockId      MDS Generated id of the HealthBlock record, in which to look up for HealthFacility
     * @param healthFacilityCode HealthFacility Census (or Proposed Census) Code
     * @return HealthFacility object corresponding to the Census (or Proposed Census)code
     */
    HealthFacility getHealthFacilityByCode(Long healthBlockId, Long healthFacilityCode);

    /**
     * get HealthSubFacility record for given HealthSubFacility Census (or Proposed Census) Code
     *
     * @param healthFacilityId      MDS Generated id of the HealthFacility record, in which to look up for HealthSubFacility
     * @param healthSubFacilityCode HealthSubFacility Census (or Proposed Census) Code
     * @return HealthSubFacility object corresponding to the Census (or Proposed Census)code
     */
    HealthSubFacility getHealthSubFacilityByCode(Long healthFacilityId, Long healthSubFacilityCode);

    /**
     * get Village record for given Village Census (or MCTS ) Code
     *
     * @param talukaId    MDS Generated id of the Taluka record, in which to look up for Village
     * @param villageCode Village Census (or MCTS ) Code
     * @return Village object corresponding to the Census (or MCTS )code
     */
    Village getVillageByCode(Long talukaId, Long villageCode);

    /**
     * get Village record for given Village Census (or MCTS ) Code
     *
     * @param stateCode State Census (or MCTS ) Code
     * @return Integer object corresponding to the Census (or MCTS )code
     */
    Integer getMaCappingByCode(Long stateCode);

    /**
     * get Village record for given Village Census (or MCTS ) Code
     *
     * @param stateCode State Census (or MCTS ) Code
     * @return Integer object corresponding to the Census (or MCTS )code
     */
    Integer getMkCappingByCode(Long stateCode);
}
