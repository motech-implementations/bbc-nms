package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.ContentUploadCsv;

import java.util.List;

/**
 * The purpose of this class is to provide methods to create, delete and retrieve  record of ContentUploadCsv.
 */
public interface ContentUploadCsvService {

    /**
     * creates object fot the ContentUploadCsv
     * @param contentUploadCsv
     * @return ContentUploadCsv object
     */
    public ContentUploadCsv createContentUploadCsv(ContentUploadCsv contentUploadCsv);

    /**
     * Finds the record of content  upload Csv by its Id
     * @param id
     * @return ContentUploadCsv
     */
    public ContentUploadCsv findByIdInCsv(Long id);

    /**
     * Deletes records from contentUpload Csv
     * @param contentUploadCsv
     */
    public void deleteFromCsv(ContentUploadCsv contentUploadCsv);

    /**
     * Retrieves the records from Csv
     * @return List of ContentUploadCsv
     */
    public List<ContentUploadCsv> retrieveAllFromCsv();
}
