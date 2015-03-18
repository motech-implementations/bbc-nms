package org.motechproject.nms.mobilekunji.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.domain.MdsEntity;

import java.util.Set;

/**
 * Created by abhishek on 18/3/15.
 */

@Entity(recordHistory = true)
public class ServiceConsumptionCall extends MdsEntity {

    Long callId;
    Long nmsFlwId;
    String circle;
    DateTime callStartTime;
    DateTime callEndTime;
    Set<CardContent> cardContent;



}
