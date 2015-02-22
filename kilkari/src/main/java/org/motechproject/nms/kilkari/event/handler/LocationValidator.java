package org.motechproject.nms.kilkari.event.handler;

import org.motechproject.nms.masterdata.domain.District;
import org.motechproject.nms.masterdata.domain.HealthBlock;
import org.motechproject.nms.masterdata.domain.HealthFacility;
import org.motechproject.nms.masterdata.domain.HealthSubFacility;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.Taluka;
import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.service.LocationService;
import org.motechproject.nms.util.helper.DataValidationException;
import org.motechproject.nms.util.helper.ParseDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationValidator {
    
    @Autowired
    private LocationService locationService;
    
    public State stateConsistencyCheck(String stateId,
            Long stateCode) throws DataValidationException {
        State state = locationService.getStateByCode(stateCode);
        if(state == null){
            ParseDataHelper.raiseInvalidDataException("State Id", stateId);
        }
        return state;
    }

    public District districtConsistencyCheck(String districtId, State state, Long districtCode) throws DataValidationException {
        District district = locationService.getDistrictByCode(state.getId(), districtCode);
        if(district == null){
            ParseDataHelper.raiseInvalidDataException( "District Id", districtId);
        }
        return district;
    }

    public Village villageConsistencyCheck(String villageId, String talukaId, Taluka taluka, Long villageCode) throws DataValidationException {
        Village village = null;
        if (villageCode!=null) {
            if(taluka!=null){
                village = locationService.getVillageByCode(taluka.getId(),villageCode);
                if(village == null){
                    ParseDataHelper.raiseInvalidDataException("Village id", villageId);
                }
            }else{
                ParseDataHelper.raiseMissingDataException("Taluka Id", talukaId);
            }
        }
        return village;
    }

    public HealthSubFacility subCenterCodeCheck(String subCenterId, String phcId,
            HealthFacility healthFacility, Long subCenterCode)
            throws DataValidationException {
        HealthSubFacility healthSubFacility = null;
        if (subCenterCode!=null) {
            if(healthFacility != null){
                healthSubFacility = locationService.getHealthSubFacilityByCode(healthFacility.getId(), subCenterCode);
                if(healthSubFacility == null){
                    ParseDataHelper.raiseInvalidDataException("Sub centered ID", subCenterId);
                }
            }else {
                ParseDataHelper.raiseMissingDataException("Phc Id", phcId);
            }
        }
        return healthSubFacility;
    }

    public HealthFacility phcConsistencyCheck(String phcId, String healthBlockId, 
            HealthBlock healthBlock, Long phcCode)
            throws DataValidationException {
        HealthFacility healthFacility = null;
        if (phcCode!=null) {
            if(healthBlock != null){
                healthFacility = locationService.getHealthFacilityByCode(healthBlock.getId(), phcCode);
                if(healthFacility == null){
                    ParseDataHelper.raiseInvalidDataException("Phc Id", phcId);
                }
            }else{
                ParseDataHelper.raiseMissingDataException("Block ID", healthBlockId);//Missing Block ID
            }
        }
        return healthFacility;
    }

    public HealthBlock healthBlockConsistencyCheck(String healthBlockId, String talukaId, 
            Taluka taluka, Long healthBlockCode) throws DataValidationException {
        HealthBlock healthBlock = null;
        if (healthBlockCode != null) {
            if (taluka != null) {
                healthBlock = locationService.getHealthBlockByCode(taluka.getId(),healthBlockCode);
                if(healthBlock == null){
                    ParseDataHelper.raiseInvalidDataException("Block ID", healthBlockId);
                }
            }else {
                //Missing taluka id"
                ParseDataHelper.raiseMissingDataException("Taluka Id", talukaId);
            }
        }
        return healthBlock;
    }

    public Taluka talukaConsistencyCheck(String talukaId, District district, String talukaCode) throws DataValidationException {
        Taluka taluka = null;
        if (talukaCode!=null) {
            taluka = locationService.getTalukaByCode(district.getId(),talukaCode);
            if(taluka == null){
                ParseDataHelper.raiseInvalidDataException("Taluka Id", talukaId);
            }
        }
        return taluka;
    }

}
