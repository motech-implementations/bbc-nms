package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.CsvContentUpload;

/**
 * This interface provides methods to get and delete the ContentUploadCsv record
 */
public interface CsvContentUploadService {

    /**
     * This method gets ContentUploadKKCsv type object by id from the database
     *
     * @param id primary key of the record
     */
    CsvContentUpload getRecord(Long id);

    /**
     * This method delete record in the database of type ContentUploadKKCsv
     *
     * @param record object of type ContentUploadKKCsv
     */
    void delete(CsvContentUpload record);

}
