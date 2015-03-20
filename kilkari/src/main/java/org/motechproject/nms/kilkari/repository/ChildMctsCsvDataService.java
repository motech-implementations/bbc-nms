package org.motechproject.nms.kilkari.repository;

import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.kilkari.domain.ChildMctsCsv;

/**
 * Interface for repository that persists simple records and allows CRUD.
 * MotechDataService base class will provide the implementation of this class as well
 * as methods for adding, deleting, saving and finding all instances.  In this class we
 * define and custom lookups we may need.
 */
public interface ChildMctsCsvDataService extends MotechDataService<ChildMctsCsv> {
    
    

}
