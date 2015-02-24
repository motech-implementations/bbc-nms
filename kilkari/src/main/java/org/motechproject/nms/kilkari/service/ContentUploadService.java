package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ContentUpload;

/**
 * This interface is used for crud operations on ContentUpload
 */

public interface ContentUploadService {

    /**
     * This method creates record in the database of type ContentUploadKK
     *
     * @param record object of type ContentUploadKK
     */
    void create(ContentUpload record);

    /**
     * This method update record in the database of type ContentUploadKK
     *
     * @param record object of type ContentUploadKK
     */
    void update(ContentUpload record);

    /**
     * This method delete record in the database of type ContentUploadKK
     *
     * @param record object of type ContentUploadKK
     */
    void delete(ContentUpload record);

    /**
     * This method get ContentUploadKK type record based on content id
     *
     * @param contentId Unique key for the record
     * @return ContentUploadKK object
     */
    ContentUpload getRecordByContentId(Long contentId);

}
