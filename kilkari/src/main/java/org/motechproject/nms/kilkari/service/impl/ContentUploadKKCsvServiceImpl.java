package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.ContentUploadKKCsv;
import org.motechproject.nms.kilkari.repository.ContentUploadKKCsvDataService;
import org.motechproject.nms.kilkari.service.ContentUploadKKCsvService;
import org.springframework.beans.factory.annotation.Autowired;

public class ContentUploadKKCsvServiceImpl implements ContentUploadKKCsvService{

    @Autowired
    private ContentUploadKKCsvDataService contentUploadKKCsvDataService;

    @Override
    public ContentUploadKKCsv findById(Long id) {
        return contentUploadKKCsvDataService.findById(id);
    }

    @Override
    public void delete(ContentUploadKKCsv record) {
        contentUploadKKCsvDataService.delete(record);
    }

    @Override
    public void deleteAll() {
        contentUploadKKCsvDataService.deleteAll();
    }
}
