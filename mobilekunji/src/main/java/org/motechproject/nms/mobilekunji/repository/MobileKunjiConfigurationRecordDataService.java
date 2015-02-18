package org.motechproject.nms.mobilekunji.repository;

import org.motechproject.nms.mobilekunji.domain.MobileKunjiConfigurationRecord;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface MobileKunjiConfigurationRecordDataService extends MotechDataService<MobileKunjiConfigurationRecord> {
    @Lookup
    MobileKunjiConfigurationRecord findRecordByIndex(@LookupField(name = "index") String index);
}
