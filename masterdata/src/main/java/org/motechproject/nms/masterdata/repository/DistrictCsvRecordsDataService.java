package org.motechproject.nms.masterdata.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.masterdata.domain.DistrictCsv;

import java.util.List;

/**
 * Created by abhishek on 27/1/15.
 */
public interface DistrictCsvRecordsDataService extends MotechDataService<DistrictCsv> {
    @Lookup
    List<DistrictCsv> findRecordByName(@LookupField(name = "stateId") String stateId);
}
