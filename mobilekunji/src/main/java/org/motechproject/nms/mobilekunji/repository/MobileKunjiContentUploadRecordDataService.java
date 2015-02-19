package org.motechproject.nms.mobilekunji.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.mobilekunji.domain.MobileKunjiContentUpload;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface MobileKunjiContentUploadRecordDataService extends MotechDataService<MobileKunjiContentUpload> {

    @Lookup
    MobileKunjiContentUpload findRecordByContentId(@LookupField(name = "contentId") int id);
}
