package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.ContentUploadCsv;
import org.motechproject.nms.mobilekunji.repository.ContentUploadCsvRecordDataService;
import org.motechproject.nms.mobilekunji.service.ContentUploadCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class provides the implementation of ContentUploadCsvService
 */
@Service("contentUploadCsvService")
public class ContentUploadCsvServiceImpl implements ContentUploadCsvService {

    @Autowired
    ContentUploadCsvRecordDataService contentUploadCsvRecordDataService;

    /**
     * creates object fot the ContentUploadCsv
     *
     * @param contentUploadCsv
     * @return ContentUploadCsv object
     */
    @Override
    public ContentUploadCsv createContentUploadCsv(ContentUploadCsv contentUploadCsv) {
        return contentUploadCsvRecordDataService.create(contentUploadCsv);
    }

    /**
     * Finds the record of content  upload Csv by its Id
     *
     * @param id
     * @return ContentUploadCsv
     */
    @Override
    public ContentUploadCsv findByIdInCsv(Long id) {
        return contentUploadCsvRecordDataService.findById(id);
    }

    /**
     * Deletes records from contentUpload Csv
     *
     * @param contentUploadCsv
     */
    @Override
    public void deleteFromCsv(ContentUploadCsv contentUploadCsv) {
        contentUploadCsvRecordDataService.delete(contentUploadCsv);
    }

    /**
     * Retrieves the records from Csv
     *
     * @return List of ContentUploadCsv
     */
    @Override
    public List<ContentUploadCsv> retrieveAllFromCsv() {
        return contentUploadCsvRecordDataService.retrieveAll();
    }
}