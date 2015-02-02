package org.motechproject.nms.masterdata.service;

import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.StateCsv;

import java.util.List;

/**
 * Created by abhishek on 26/1/15.
 */
public interface StateRecordService {

    public void add(State record);

    public List<StateCsv> retrieveAllRecords();

}
