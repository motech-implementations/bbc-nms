package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.ContentUpload;
import org.motechproject.nms.kilkari.repository.ContentUploadDataService;
import org.motechproject.nms.kilkari.service.ContentUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("contentUploadService")
public class ContentUploadServiceImpl implements ContentUploadService {

    @Autowired
    private ContentUploadDataService contentUploadKKCsvDataService;

    /**
     * This method creates record in the database of type ContentUploadKK
     *
     * @param record object of type ContentUploadKK
     */
    @Override
    public void create(ContentUpload record) {
        contentUploadKKCsvDataService.create(record);
    }

    /**
     * This method update record in the database of type ContentUploadKK
     *
     * @param record object of type ContentUploadKK
     */
    @Override
    public void update(ContentUpload record) {
        contentUploadKKCsvDataService.update(record);
    }

    /**
     * This method delete record in the database of type ContentUploadKK
     *
     * @param record object of type ContentUploadKK
     */
    @Override
    public void delete(ContentUpload record) {
        contentUploadKKCsvDataService.delete(record);
    }

    /**
     * This method get ContentUploadKK type record based on content id
     *
     * @param contentId Unique key for the record
     * @return ContentUploadKK object
     */
    @Override
    public ContentUpload getRecordByContentId(Long contentId) {
        return contentUploadKKCsvDataService.findByContentId(contentId);
    }
}
