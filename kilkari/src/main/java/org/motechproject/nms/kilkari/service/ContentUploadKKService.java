package org.motechproject.nms.kilkari.service;

import org.motechproject.nms.kilkari.domain.ContentUploadKK;

public interface ContentUploadKKService {

    void create(ContentUploadKK record);

    void update(ContentUploadKK record);

    ContentUploadKK getRecordByContentId(Long contentId);

}
