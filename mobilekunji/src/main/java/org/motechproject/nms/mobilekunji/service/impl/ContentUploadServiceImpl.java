package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.ContentUpload;
import org.motechproject.nms.mobilekunji.repository.ContentUploadRecordDataService;
import org.motechproject.nms.mobilekunji.service.ContentUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class provides the implementation of ContentUploadService
 */
@Service("contentUploadService")
public class ContentUploadServiceImpl implements ContentUploadService {

    @Autowired
    ContentUploadRecordDataService contentUploadRecordDataService;

    @Override
    public void createContentUpload(ContentUpload contentUpload) {
        contentUploadRecordDataService.create(contentUpload);
    }

    @Override
    public void updateContentUpload(ContentUpload contentUpload) {
        contentUploadRecordDataService.update(contentUpload);
    }

    @Override
    public ContentUpload findRecordByContentId(Integer contentId) {
        return contentUploadRecordDataService.findRecordByContentId(contentId);
    }
}
