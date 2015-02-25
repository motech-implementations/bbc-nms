package org.motechproject.nms.kilkari.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.kilkari.domain.ContentUploadKK;

public interface ContentUploadDataService extends MotechDataService<ContentUploadKK> {

    @Lookup
    ContentUploadKK findByContentId(@LookupField(name = "contentId") Long id);
}
