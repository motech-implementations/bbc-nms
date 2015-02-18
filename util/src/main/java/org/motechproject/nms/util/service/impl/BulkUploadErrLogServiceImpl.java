package org.motechproject.nms.util.service.impl;

import org.motechproject.nms.util.constants.Constants;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.nms.util.BulkUploadErrRecordDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.Date;

public class BulkUploadErrLogServiceImpl implements BulkUploadErrLogService {

    private Logger logger = LoggerFactory.getLogger(BulkUploadErrLogServiceImpl.class);

    @Override
    public void writeBulkUploadErrLog(String logFileName,
            BulkUploadErrRecordDetails bulkUploadErrRecordDetails) {
        
        try {

            Date date = new Date();
            Path logFilePath = FileSystems.getDefault().getPath(".", logFileName.toString());

            StringBuffer errLog = new StringBuffer();
            errLog.append(Constants.ERROR_LOG_TITLE);
            errLog.append(new Timestamp(date.getTime()));
            errLog.append(Constants.SPACE);
            errLog.append(bulkUploadErrRecordDetails.getRecordDetails());
            errLog.append(Constants.SPACE);
            errLog.append(bulkUploadErrRecordDetails.getErrorCode());
            errLog.append(Constants.SPACE);
            errLog.append(bulkUploadErrRecordDetails.getErrorDescription());

            Files.write(logFilePath,
                    errLog.toString().getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

            Files.write(logFilePath,
                    Constants.NEXT_LINE.getBytes(),
                    StandardOpenOption.APPEND);

        } catch (IOException ioe) {
            logger.error("IOException while writing error log to file : " + logFileName + "Error : " + ioe.getMessage());
        }
        
    }

    @Override
    public void writeBulkUploadProcessingSummary(String logFileName,
            Integer numberOfSuccessfulRecords,
            Integer numberOfFailedRecords) {
        try {

            Date date = new Date();
            Path logFilePath = FileSystems.getDefault().getPath(".", logFileName.toString());

            StringBuffer uploadProcessingSummary = new StringBuffer();
            uploadProcessingSummary.append(Constants.BULK_UPLOAD_SUMMARY_TITLE);
            uploadProcessingSummary.append(new Timestamp(date.getTime()));
            uploadProcessingSummary.append(Constants.SPACE);
            uploadProcessingSummary.append(Constants.SUCCESSFUL_RECORDS);
            uploadProcessingSummary.append(numberOfSuccessfulRecords.toString());
            uploadProcessingSummary.append(Constants.FAILED_RECORDS);
            uploadProcessingSummary.append(numberOfFailedRecords.toString());
            
            Files.write(logFilePath,
                    uploadProcessingSummary.toString().getBytes(),
                    StandardOpenOption.CREATE, 
                    StandardOpenOption.APPEND);
            
            Files.write(logFilePath,
                    Constants.NEXT_LINE.getBytes(),
                    StandardOpenOption.APPEND);

        } catch (IOException ioe) {
            logger.error("IOException while writing error log to file : " + logFileName + "Error : " + ioe.getMessage());
        }
    }
}
