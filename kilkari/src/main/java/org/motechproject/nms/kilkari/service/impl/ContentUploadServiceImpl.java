package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.ContentUploadKK;
import org.motechproject.nms.kilkari.repository.ContentUploadDataService;
import org.motechproject.nms.kilkari.service.ContentUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("contentUploadKKService")
public class ContentUploadServiceImpl implements ContentUploadService {

    @Autowired
    private ContentUploadDataService contentUploadKKCsvDataService;

    /**
     * This method creates record in the database of type ContentUploadKK
     *
     * @param record object of type ContentUploadKK
     */
    @Override
    public void create(ContentUploadKK record) {
        contentUploadKKCsvDataService.create(record);
    }

    /**
     * This method update record in the database of type ContentUploadKK
     *
     * @param record object of type ContentUploadKK
     */
    @Override
    public void update(ContentUploadKK record) {
        contentUploadKKCsvDataService.update(record);
    }

    /**
     * This method delete record in the database of type ContentUploadKK
     *
     * @param record object of type ContentUploadKK
     */
    @Override
    public void delete(ContentUploadKK record) {
        contentUploadKKCsvDataService.delete(record);
    }

    /**
     * This method get ContentUploadKK type record based on content id
     *
     * @param contentId Unique key for the record
     * @return ContentUploadKK object
     */
    @Override
    public ContentUploadKK getRecordByContentId(Long contentId) {
        return contentUploadKKCsvDataService.findByContentId(contentId);
    }
}
