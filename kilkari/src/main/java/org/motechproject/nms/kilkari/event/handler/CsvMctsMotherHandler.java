package org.motechproject.nms.kilkari.event.handler;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.service.CsvMctsMotherService;
import org.motechproject.nms.util.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static org.motechproject.nms.kilkari.commons.Constants.MOTHER_MCTS_CSV_UPLOAD_SUCCESS_EVENT;

/**
 * This class is used to handle the success
 * event of csv upload of MotherMcts
 */
@Component
public class CsvMctsMotherHandler {

    private static Logger logger = LoggerFactory.getLogger(CsvMctsMotherHandler.class);
    private CsvMctsMotherService motherMctsCsvService;
    
    
    @Autowired
    public CsvMctsMotherHandler(CsvMctsMotherService motherMctsCsvService){
        
        this.motherMctsCsvService = motherMctsCsvService;
    }

    /**
     * This method is used to process record when csv upload successfully.
     * 
     * @param motechEvent This is motechEvent having uploaded record details 
     */
    @MotechListener(subjects = MOTHER_MCTS_CSV_UPLOAD_SUCCESS_EVENT)
    public void motherMctsCsvSuccess(MotechEvent motechEvent) {
        logger.trace("Success[motherMctsCsvSuccess] method start for MotherMctsCsv");
        logger.info("Event invoked [{}]" + MOTHER_MCTS_CSV_UPLOAD_SUCCESS_EVENT);
        
        Map<String, Object> parameters = motechEvent.getParameters();
        List<Long> uploadedIDs = (List<Long>) parameters.get(Constants.CSV_IMPORT_CREATED_IDS);
        String csvFileName = (String) parameters.get(Constants.CSV_IMPORT_FILE_NAME);

        motherMctsCsvService.processMotherMctsCsv(csvFileName, uploadedIDs);
        logger.trace("Success[motherMctsCsvSuccess] method finished for MotherMctsCsv");
    }
    

}
