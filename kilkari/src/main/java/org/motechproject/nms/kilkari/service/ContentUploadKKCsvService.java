package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ContentUploadKKCsv;

public interface ContentUploadKKCsvService {

    ContentUploadKKCsv findById(Long id);

    void delete(ContentUploadKKCsv record);

    void deleteAll();
}
