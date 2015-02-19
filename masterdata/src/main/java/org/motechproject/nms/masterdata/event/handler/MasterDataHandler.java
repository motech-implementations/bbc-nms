package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.StateCsv;
//import org.motechproject.nms.masterdata.service.StateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * Created by abhishek on 26/1/15.
 */
@Component
public class MasterDataHandler {

//    @Autowired
//    private StateRecordService stateRecordService;

//    @MotechListener(subjects = {"mds.crud.masterdata.StateCsv.CREATE"})
//    public void stateDataHandler(MotechEvent event)
//    {
//        List<StateCsv> stateCsvList = stateRecordService.retrieveAllRecords();
//
//        for(Iterator<StateCsv>itr=stateCsvList.iterator();itr.hasNext();)
//        {
//            StateCsv record = itr.next();
//
//            State data = getStateData(record);
//
//            stateRecordService.add(data);
//        }
//    }

    private State getStateData(StateCsv csvData)
    {
        State data= new State(csvData.getName(),Long.parseLong(csvData.getStateId()),null);

        return data;
    }
}
