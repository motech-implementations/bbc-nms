package org.motechproject.nms.frontlineworker.repository;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.nms.frontlineworker.domain.FrontLineWorker;

/**
 * Created by abhishek on 2/2/15.
 */


public interface FlwRecordDataService extends MotechDataService<FrontLineWorker> {

    @Lookup
    FrontLineWorker getFlwByFlwIdAndStateId(@LookupField(name = "flwId") Long flwId,@LookupField(name = "stateCode") Long stateCode);

    @Lookup
    FrontLineWorker getFlwByContactNo(@LookupField(name = "ContactNo") String contactNo);

    }
