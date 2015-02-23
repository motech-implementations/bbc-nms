package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ContentUploadKKCsv;

public interface ContentUploadKKCsvService {

    /**
     * This method gets ContentUploadKKCsv type object by id from the database
     *
     * @param id primary key of the record
     */
    ContentUploadKKCsv getRecord(Long id);

    /**
     * This method delete record in the database of type ContentUploadKKCsv
     *
     * @param record object of type ContentUploadKKCsv
     */
    void delete(ContentUploadKKCsv record);

}
