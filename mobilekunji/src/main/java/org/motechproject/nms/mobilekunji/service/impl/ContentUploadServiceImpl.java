package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.ContentUpload;
import org.motechproject.nms.mobilekunji.repository.ContentUploadRecordDataService;
import org.motechproject.nms.mobilekunji.service.ContentUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  This class acts as implementation class for the interface ContentUploadService.
 *  It uses ContentUploadRecordDataService which further takes MotechDataService as
 *  base class to performs the CRUD operations on ContentUpload records. It also adds lookup
 *  procedures to fetch the ContentUpload record from Database.
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
