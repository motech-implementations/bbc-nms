package org.motechproject.nms.mobilekunji.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.domain.MdsEntity;

/**
 * Created by abhishek on 18/3/15.
 */

@Entity(recordHistory = true)
public class ServiceConsumptionFrontLineWorker  extends MdsEntity {

    Long    nmsFlwId;
    Integer endofUsagePromt;
    Boolean welcomePromptFlag;
    Integer currentUsageInPulses;

}
