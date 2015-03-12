package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.ContentUpload;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * Its implementation uses the repository interface ContentUploadRecordDataService whose base class
 * MotechDataService will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances. In this interface we
 * also declare lookups we may need to find record from Database.
 */
public interface ContentUploadService {

    public void createContentUpload(ContentUpload contentUpload);

    public void updateContentUpload(ContentUpload contentUpload);

    public ContentUpload findRecordByContentId(Integer contentId);

}
