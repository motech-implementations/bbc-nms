package org.motechproject.nms.mobileacademy.event.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.nms.mobileacademy.domain.MobileAcademyConstants;
import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class provides Motech Listeners for course raw content csv handler for
 * both success and failure scenarios.
 */

@Component
public class CourseUploadCsvHandler {

    public static Integer successCount = 0;

    public static Integer failCount = 0;

    @Autowired
    private BulkUploadErrLogService bulkUploadErrLogService;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CourseUploadCsvHandler.class);

    /**
     * This method provides a listener to the course csv upload success
     * scenario.
     * 
     * @param motechEvent name of the event raised during upload
     */
    @MotechListener(subjects = { MobileAcademyConstants.COURSE_CSV_UPLOAD_SUCCESS })
    public void courseCsvDataSuccessHandler(MotechEvent motechEvent) {
        LOGGER.info("Entered into course Csv Data Success Handler");
        String userName = null;
        CsvProcessingSummary summary = new CsvProcessingSummary();
        Map<String, Object> params = motechEvent.getParameters();
        List<Long> createdIds = (ArrayList<Long>) params
                .get("csv-import.created_ids");
        String csvImportFileName = (String) params.get("csv-import.filename");
        String errorFileName = BulkUploadError
                .createBulkUploadErrLogFileName(csvImportFileName);
        System.out.println("Rows inserted---" + createdIds);
        // TODO yogesh success code
        bulkUploadErrLogService.writeBulkUploadProcessingSummary(userName,
                csvImportFileName, errorFileName, summary);
    }

    /**
     * This method provides a listener to the course csv upload failure
     * scenario.
     * 
     * @param motechEvent name of the event raised during upload
     */
    @MotechListener(subjects = { MobileAcademyConstants.COURSE_CSV_UPLOAD_FAILED })
    public void courseCsvDataFailureHandler(MotechEvent motechEvent) {
        LOGGER.info("Entered into course Csv Data Failure Handler");
        Map<String, Object> params = motechEvent.getParameters();
        List<Long> createdIds = (ArrayList<Long>) params
                .get("csv-import.created_ids");
        // TODO yogesh error code
    }

}
