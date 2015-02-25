package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ContentUploadKK;

public interface ContentUploadService {

    /**
     * This method creates record in the database of type ContentUploadKK
     *
     * @param record object of type ContentUploadKK
     */
    void create(ContentUploadKK record);

    /**
     * This method update record in the database of type ContentUploadKK
     *
     * @param record object of type ContentUploadKK
     */
    void update(ContentUploadKK record);

    /**
     * This method delete record in the database of type ContentUploadKK
     *
     * @param record object of type ContentUploadKK
     */
    void delete(ContentUploadKK record);

    /**
     * This method get ContentUploadKK type record based on content id
     *
     * @param contentId Unique key for the record
     * @return ContentUploadKK object
     */
    ContentUploadKK getRecordByContentId(Long contentId);

}
