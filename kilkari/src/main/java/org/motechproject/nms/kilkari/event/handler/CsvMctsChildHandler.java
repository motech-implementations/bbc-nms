package org.motechproject.nms.kilkari.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.service.CsvMctsChildService;
import org.motechproject.nms.util.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.motechproject.nms.kilkari.commons.Constants.CHILD_MCTS_CSV_UPLOAD_SUCCESS_EVENT;

/**
 * This class is used to handle the success
 * event of csv upload of ChildMcts
 */
@Component
public class CsvMctsChildHandler {

    private CsvMctsChildService csvMctsChildService;
    private static Logger logger = LoggerFactory.getLogger(CsvMctsChildHandler.class);

    @Autowired
    public CsvMctsChildHandler(CsvMctsChildService csvMctsChildService){
        
        this.csvMctsChildService = csvMctsChildService;
    }

    /**
     * This method is used to process record when CsvMctsChild upload is successful.
     * 
     * @param motechEvent This is motechEvent having uploaded record details 
     */
    @MotechListener(subjects = CHILD_MCTS_CSV_UPLOAD_SUCCESS_EVENT)
    public void csvMctsChildSuccess(MotechEvent motechEvent) {
        logger.trace("Success[CsvMctsChildSuccess] method start for CsvMctsChild");
        logger.info("Event invoked [{}]" + CHILD_MCTS_CSV_UPLOAD_SUCCESS_EVENT);
        
        Map<String, Object> parameters = motechEvent.getParameters();
        List<Long> uploadedIDs = (List<Long>) parameters.get(Constants.CSV_IMPORT_CREATED_IDS);
        String csvFileName = (String) parameters.get(Constants.CSV_IMPORT_FILE_NAME);

        csvMctsChildService.processCsvMctsChild(csvFileName, uploadedIDs);
        logger.trace("Success[CsvMctsChildSuccess] method finished for CsvMctsChild");
    }

}
