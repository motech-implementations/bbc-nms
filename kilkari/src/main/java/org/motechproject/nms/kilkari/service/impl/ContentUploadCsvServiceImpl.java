package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.ContentUploadKKCsv;
import org.motechproject.nms.kilkari.repository.ContentUploadCsvDataService;
import org.motechproject.nms.kilkari.service.ContentUploadCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("contentUploadKKCsvService")
public class ContentUploadCsvServiceImpl implements ContentUploadCsvService {

    @Autowired
    private ContentUploadCsvDataService contentUploadCsvDataService;

    /**
     * This method gets ContentUploadKKCsv type object by id from the database
     *
     * @param id primary key of the record
     */
    @Override
    public ContentUploadKKCsv getRecord(Long id) {
        return contentUploadCsvDataService.findById(id);
    }

    /**
     * This method delete record in the database of type ContentUploadKKCsv
     *
     * @param record object of type ContentUploadKKCsv
     */
    @Override
    public void delete(ContentUploadKKCsv record) {
        contentUploadCsvDataService.delete(record);
    }

}
