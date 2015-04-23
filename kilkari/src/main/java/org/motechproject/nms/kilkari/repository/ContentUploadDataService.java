package org.motechproject.nms.kilkari.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.kilkari.domain.ContentUpload;

import java.util.List;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface ContentUploadDataService extends MotechDataService<ContentUpload> {

    @Lookup
    ContentUpload findByContentId(@LookupField(name = "contentId") Long id);

    @Lookup
    List<ContentUpload> findContentFileName(@LookupField(name = "contentName") String contentName,
            @LookupField(name = "languageLocationCode") Integer languageLocationCode);
}
