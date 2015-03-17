package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.ContentUpload;

/**
 * Created by abhishek on 13/3/15.
 */
public interface ContentUploadService {

    public void createContentUpload(ContentUpload contentUpload);

    public void updateContentUpload(ContentUpload contentUpload);

    public ContentUpload findRecordByContentId(Integer contentId);

}
