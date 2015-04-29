package org.motechproject.nms.mobilekunji.service;

import org.motechproject.nms.mobilekunji.domain.CsvContentUpload;

import java.util.List;

/**
 * The purpose of this class is to provide methods to create, delete and retrieve  record of CsvContentUpload.
 */
public interface ContentUploadCsvService {

    /**
     * creates object fot the CsvContentUpload
     *
     * @param csvContentUpload
     * @return CsvContentUpload object
     */
    public CsvContentUpload createContentUploadCsv(CsvContentUpload csvContentUpload);

    /**
     * Finds the record of content  upload Csv by its Id
     *
     * @param id
     * @return CsvContentUpload
     */
    public CsvContentUpload findByIdInCsv(Long id);

    /**
     * Deletes records from contentUpload Csv
     *
     * @param csvContentUpload
     */
    public void deleteFromCsv(CsvContentUpload csvContentUpload);

    /**
     * Retrieves the records from Csv
     *
     * @return List of CsvContentUpload
     */
    public List<CsvContentUpload> retrieveAllFromCsv();
}
