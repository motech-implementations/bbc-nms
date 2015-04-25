package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.CsvContentUpload;
import org.motechproject.nms.kilkari.repository.CsvContentUploadDataService;
import org.motechproject.nms.kilkari.service.CsvContentUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used to get and delete the ContentUploadCsv record
 */
@Service("csvContentUploadService")
public class CsvContentUploadServiceImpl implements CsvContentUploadService {

    @Autowired
    private CsvContentUploadDataService csvContentUploadDataService;

    /**
     * This method gets ContentUploadKKCsv type object by id from the database
     *
     * @param id primary key of the record
     */
    @Override
    public CsvContentUpload getRecord(Long id) {
        return csvContentUploadDataService.findById(id);
    }

    /**
     * This method delete record in the database of type ContentUploadKKCsv
     *
     * @param record object of type ContentUploadKKCsv
     */
    @Override
    public void delete(CsvContentUpload record) {
        csvContentUploadDataService.delete(record);
    }

}
