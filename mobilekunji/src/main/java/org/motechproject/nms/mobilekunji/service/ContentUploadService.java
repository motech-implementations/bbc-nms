package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.ContentUpload;

/**
 * The purpose of this class is to provide methods to create, update and find  record of ContentUpload.
 */
public interface ContentUploadService {

    /**
     * Creates record object for ContentUpload
     *
     * @param contentUpload
     */
    public void createContentUpload(ContentUpload contentUpload);

    /**
     * Update record of ContentUpload
     *
     * @param contentUpload
     */
    public void updateContentUpload(ContentUpload contentUpload);

    /**
     * Finds records of ContentUpload by its Id
     *
     * @param contentId
     * @return ContentUpload
     */
    public ContentUpload findRecordByContentId(Integer contentId);

}
