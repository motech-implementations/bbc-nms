package org.motechproject.nms.mobilekunji.service.impl;

import org.motechproject.nms.mobilekunji.domain.CsvContentUpload;
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
     * creates object fot the CsvContentUpload
     *
     * @param csvContentUpload
     * @return CsvContentUpload object
     */
    @Override
    public CsvContentUpload createContentUploadCsv(CsvContentUpload csvContentUpload) {
        return contentUploadCsvRecordDataService.create(csvContentUpload);
    }

    /**
     * Finds the record of content  upload Csv by its Id
     *
     * @param id
     * @return CsvContentUpload
     */
    @Override
    public CsvContentUpload findByIdInCsv(Long id) {
        return contentUploadCsvRecordDataService.findById(id);
    }

    /**
     * Deletes records from contentUpload Csv
     *
     * @param csvContentUpload
     */
    @Override
    public void deleteFromCsv(CsvContentUpload csvContentUpload) {
        contentUploadCsvRecordDataService.delete(csvContentUpload);
    }

    /**
     * Retrieves the records from Csv
     *
     * @return List of CsvContentUpload
     */
    @Override
    public List<CsvContentUpload> retrieveAllFromCsv() {
        return contentUploadCsvRecordDataService.retrieveAll();
    }
}