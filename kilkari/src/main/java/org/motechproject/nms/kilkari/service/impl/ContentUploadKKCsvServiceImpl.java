package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.ContentUploadKKCsv;
import org.motechproject.nms.kilkari.repository.ContentUploadKKCsvDataService;
import org.motechproject.nms.kilkari.service.ContentUploadKKCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("contentUploadKKCsvService")
public class ContentUploadKKCsvServiceImpl implements ContentUploadKKCsvService {

    @Autowired
    private ContentUploadKKCsvDataService contentUploadKKCsvDataService;

    /**
     * This method gets ContentUploadKKCsv type object by id from the database
     *
     * @param id primary key of the record
     */
    @Override
    public ContentUploadKKCsv getRecord(Long id) {
        return contentUploadKKCsvDataService.findById(id);
    }

    /**
     * This method delete record in the database of type ContentUploadKKCsv
     *
     * @param record object of type ContentUploadKKCsv
     */
    @Override
    public void delete(ContentUploadKKCsv record) {
        contentUploadKKCsvDataService.delete(record);
    }

}
