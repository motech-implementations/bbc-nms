package org.motechproject.nms.kilkari.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.kilkari.domain.MotherMctsCsv;

public interface MotherMctsCsvDataService extends
		MotechDataService<MotherMctsCsv> {
	@Lookup
    MotherMctsCsv findRecordByName(@LookupField(name = "name") String recordName);
}
