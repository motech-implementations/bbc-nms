package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.ContentUploadCsv;

import java.util.List;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * Its implementation uses the repository interface ContentUploadCsvRecordDataService whose base class
 * MotechDataService will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.
 */
public interface ContentUploadCsvService {

    public ContentUploadCsv createContentUploadCsv(ContentUploadCsv contentUploadCsv);

    public ContentUploadCsv findByIdInCsv(Long id);

    public void deleteFromCsv(ContentUploadCsv contentUploadCsv);

    public List<ContentUploadCsv> retrieveAllFromCsv();
}
