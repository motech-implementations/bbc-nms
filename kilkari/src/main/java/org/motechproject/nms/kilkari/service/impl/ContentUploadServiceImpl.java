package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.ContentUpload;
import org.motechproject.nms.kilkari.repository.ContentUploadDataService;
import org.motechproject.nms.kilkari.service.ContentUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used to perform crud operations on ContentUpload
 */
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
    
    /**
     * This method get ContentUpload type record based on content name and language location code
     *
     * @param contentName String type object
     * @param languageLocationCode Integer type object
     * @return ContentUpload ContentUpload type object
     */
    @Override
    public String getContentFileName(String contentName, Integer languageLocationCode) {
        String contentFile = null;
        ContentUpload contentUpload = contentUploadKKCsvDataService.findContentFileName(contentName, languageLocationCode);
        if(contentUpload != null){
            contentFile  = contentUpload.getContentFile();
        }
        return contentFile; 
    }
}
