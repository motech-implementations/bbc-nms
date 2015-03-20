package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ContentUploadCsv;

/**
 * This interface provides methods to get and delete the ContentUploadCsv record
 */
public interface ContentUploadCsvService {

    /**
     * This method gets ContentUploadKKCsv type object by id from the database
     *
     * @param id primary key of the record
     */
    ContentUploadCsv getRecord(Long id);

    /**
     * This method delete record in the database of type ContentUploadKKCsv
     *
     * @param record object of type ContentUploadKKCsv
     */
    void delete(ContentUploadCsv record);

}
