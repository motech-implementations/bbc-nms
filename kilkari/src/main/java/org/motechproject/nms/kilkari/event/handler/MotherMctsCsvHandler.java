package org.motechproject.nms.kilkari.event.handler;

import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.kilkari.service.MotherMctsCsvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is used to handle to success 
 * and failure event of csv upload of MotherMcts
 */
@Component
public class MotherMctsCsvHandler {

    private static final String CSV_IMPORT_PREFIX = "csv-import.";
    public static final String CSV_IMPORT_CREATED_IDS = CSV_IMPORT_PREFIX + "created_ids";
    public static final String CSV_IMPORT_UPDATED_IDS = CSV_IMPORT_PREFIX + "updated_ids";
    public static final String CSV_IMPORT_CREATED_COUNT = CSV_IMPORT_PREFIX + "created_count";
    public static final String CSV_IMPORT_UPDATED_COUNT = CSV_IMPORT_PREFIX + "updated_count";
    public static final String CSV_IMPORT_TOTAL_COUNT = CSV_IMPORT_PREFIX + "total_count";
    public static final String CSV_IMPORT_FAILURE_MSG = CSV_IMPORT_PREFIX + "failure_message";
    public static final String CSV_IMPORT_FAILURE_STACKTRACE = CSV_IMPORT_PREFIX + "failure_stacktrace";
    public static final String CSV_IMPORT_FILE_NAME = CSV_IMPORT_PREFIX + "filename";

    private MotherMctsCsvService motherMctsCsvService;

    private static Logger logger = LoggerFactory.getLogger(MotherMctsCsvHandler.class);

    @Autowired
    public MotherMctsCsvHandler(MotherMctsCsvService motherMctsCsvService){
        
        this.motherMctsCsvService = motherMctsCsvService;

    }

    /**
     * This method is used to process record when csv upload successfully.
     * 
     * @param motechEvent This is motechEvent having uploaded record details 
     */
    @MotechListener(subjects = "mds.crud.kilkari.MotherMctsCsv.csv-import.success")
    public void motherMctsCsvSuccess(MotechEvent motechEvent) {
        logger.info("Success[motherMctsCsvSuccess] method start for MotherMctsCsv");
        
        Map<String, Object> parameters = motechEvent.getParameters();
        List<Long> uploadedIDs = (List<Long>) parameters.get(CSV_IMPORT_CREATED_IDS);
        String csvFileName = (String) parameters.get(CSV_IMPORT_FILE_NAME);

        motherMctsCsvService.processMotherMctsCsv(csvFileName, uploadedIDs);
        logger.info("Success[motherMctsCsvSuccess] method finished for MotherMctsCsv");
    }
    

}
