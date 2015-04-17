package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.repository.VillageRecordsDataService;
import org.motechproject.nms.masterdata.service.VillageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on Village
 */
@Service("villageService")
public class VillageServiceImpl implements VillageService {

    private VillageRecordsDataService villageRecordsDataService;

    @Autowired
    public VillageServiceImpl(VillageRecordsDataService villageRecordsDataService) {
        this.villageRecordsDataService = villageRecordsDataService;
    }

    /**
     * creates a new record for the village
     *
     * @param record of the Village
     * @return
     */
    @Override
    public Village create(Village record) {
        return villageRecordsDataService.create(record);
    }

    /**
     * updates the record of the Village
     *
     * @param record of the Village
     */
    @Override
    public void update(Village record) {
        villageRecordsDataService.update(record);

    }

    /**
     * deletes the record of the Village
     *
     * @param record of the Village
     */
    @Override
    public void delete(Village record) {
        villageRecordsDataService.delete(record);

    }

    /**
     * Gets the village details according to the parent code
     *
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param villageCode
     * @return Village
     */
    @Override
    public Village findVillageByParentCode(Long stateCode, Long districtCode, Long talukaCode, Long villageCode) {
        return villageRecordsDataService.findVillageByParentCode(stateCode, districtCode, talukaCode, villageCode);
    }

    /**
     * Get VillageRecordsDataService object
     */
    @Override
    public VillageRecordsDataService getVillageRecordsDataService() {
        return villageRecordsDataService;
    }

}
