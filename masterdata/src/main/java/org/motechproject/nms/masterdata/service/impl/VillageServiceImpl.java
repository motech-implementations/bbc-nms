package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.Village;
import org.motechproject.nms.masterdata.repository.VillageRecordsDataService;
import org.motechproject.nms.masterdata.service.VillageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by nms on 17/3/15.
 */
@Service("villageService")
public class VillageServiceImpl implements VillageService {

    private VillageRecordsDataService villageRecordsDataService;

    @Autowired
    public VillageServiceImpl(VillageRecordsDataService villageRecordsDataService) {
        this.villageRecordsDataService = villageRecordsDataService;
    }

    @Override
    public Village create(Village record) {
        return villageRecordsDataService.create(record);
    }

    @Override
    public void update(Village record) {
        villageRecordsDataService.update(record);

    }

    @Override
    public void delete(Village record) {
        villageRecordsDataService.delete(record);

    }

    /**
     * @param stateCode
     * @param districtCode
     * @param talukaCode
     * @param villageCode
     * @return
     */
    @Override
    public Village findVillageByParentCode(Long stateCode, Long districtCode, Long talukaCode, Long villageCode) {
        return villageRecordsDataService.findVillageByParentCode(stateCode, districtCode, talukaCode, villageCode);
    }
}
