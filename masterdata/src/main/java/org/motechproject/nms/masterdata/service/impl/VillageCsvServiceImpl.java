package org.motechproject.nms.masterdata.service.impl;

import org.motechproject.nms.masterdata.domain.CsvVillage;
import org.motechproject.nms.masterdata.repository.VillageCsvRecordsDataService;
import org.motechproject.nms.masterdata.service.VillageCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used for crud operations on VillageCsv
 */
@Service("villageCsvService")
public class VillageCsvServiceImpl implements VillageCsvService {

    private VillageCsvRecordsDataService villageCsvRecordsDataService;

    @Autowired
    public VillageCsvServiceImpl(VillageCsvRecordsDataService villageCsvRecordsDataService) {
        this.villageCsvRecordsDataService = villageCsvRecordsDataService;
    }

    /**
     * delete VillageCsv type object
     *
     * @param record of the VillageCsv
     */
    @Override
    public void delete(CsvVillage record) {
        villageCsvRecordsDataService.delete(record);

    }

    /**
     * create VillageCsv type object
     *
     * @param record of the VillageCsv
     */
    @Override
    public CsvVillage create(CsvVillage record) {
        return villageCsvRecordsDataService.create(record);
    }

    /**
     * Gets the Village Csv by Id
     *
     * @param id
     * @return villageCsv
     */
    @Override
    public CsvVillage findById(Long id) {
        return villageCsvRecordsDataService.findById(id);
    }
}
