package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ContentUploadKKCsv;

public interface ContentUploadKKCsvService {

    ContentUploadKKCsv getRecord(Long id);

    void delete(ContentUploadKKCsv record);

}
