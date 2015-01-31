package org.motechproject.nms.masterdata.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.masterdata.domain.State;
import org.motechproject.nms.masterdata.domain.StateCsv;
import org.motechproject.nms.masterdata.repository.StateCsvRecordsDataService;
import org.motechproject.nms.masterdata.repository.StateRecordsDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * Created by abhishek on 29/1/15.
 */

@Component
public class StateCsvUploadHandler {

    @Autowired
    private StateRecordsDataService stateRecordsDataService;

    @Autowired
    private StateCsvRecordsDataService stateCsvRecordsDataService;

    private static Logger logger = LoggerFactory.getLogger(StateCsvUploadHandler.class);

    @MotechListener(subjects = {"mds.crud.masterdata.StateCsv.csv-import.success"})
    public void stateCsvSuccess(MotechEvent event) {

        List<StateCsv> stateCsvList = stateCsvRecordsDataService.retrieveAll();

        for (Iterator<StateCsv> itr = stateCsvList.iterator(); itr.hasNext(); ) {

            StateCsv csvRecord = itr.next();

            if (csvRecord.getStateCode() != null) {

                Long stateId = getLongId(csvRecord.getStateCode());

                if (null == stateId) {

                    logger.error("stateId is invalid " + csvRecord.getStateCode());

                } else {

                    State stateExistData = stateRecordsDataService.findRecordByStateCode(Long.parseLong(csvRecord.getStateCode()));

                    if (null != stateExistData) {

                        setStateName(stateExistData, csvRecord);

                        stateRecordsDataService.update(stateExistData);

                    } else {

                        State stateNewData = getStateData(csvRecord);

                        stateRecordsDataService.create(stateNewData);
                    }
                }
            }
        }

        logger.info("State permanent data is successfully inserted.");

        stateCsvRecordsDataService.deleteAll();

        logger.info("State successfully deleted");
    }

    @MotechListener(subjects = {"mds.crud.masterdata.StateCsv.csv-import.failed"})
    public void stateCsvFailed(MotechEvent event) {

        stateCsvRecordsDataService.deleteAll();

        logger.info("State successfully deleted");
    }

    private Long getLongId(String id){
        try {

            Long longId = Long.parseLong(id.trim());

            return longId;
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private void setStateName(State data,StateCsv csvData){

        data.setName(csvData.getName());
    }

    private State getStateData(StateCsv csvData)
    {
        State data= new State(csvData.getName(),Long.parseLong(csvData.getStateCode()),null);

        return data;
    }
}
