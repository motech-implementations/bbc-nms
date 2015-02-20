package org.motechproject.nms.kilkari.service.impl;

import org.motechproject.nms.kilkari.domain.ContentUploadKK;
import org.motechproject.nms.kilkari.repository.ContentUploadKKDataService;
import org.motechproject.nms.kilkari.service.ContentUploadKKService;
import org.springframework.beans.factory.annotation.Autowired;

public class ContentUploadKKServiceImpl implements ContentUploadKKService {

    @Autowired
    private ContentUploadKKDataService contentUploadKKCsvDataService;

    @Override
    public void create(ContentUploadKK record) {
        contentUploadKKCsvDataService.create(record);
    }

    @Override
    public void update(ContentUploadKK record) {
        contentUploadKKCsvDataService.update(record);
    }
}
