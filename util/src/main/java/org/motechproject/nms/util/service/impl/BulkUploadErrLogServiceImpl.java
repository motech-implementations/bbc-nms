package org.motechproject.nms.util.service.impl;

import org.motechproject.nms.util.constants.Constants;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;

public class BulkUploadErrLogServiceImpl implements BulkUploadErrLogService {

    private Logger logger = LoggerFactory.getLogger(BulkUploadErrLogServiceImpl.class);

    @Override
    public void writeBulkUploadErrLog(String logFileName,
            String erroneousRecord, String errorCode, String errorCause) {
        
        try {

            java.util.Date date = new java.util.Date();
            Timestamp currentTimestamp = new Timestamp(date.getTime());

            StringBuffer errLog = new StringBuffer();
            errLog.append(Constants.ERROR_LOG_TITLE);
            errLog.append(Constants.SPACE);
            errLog.append(currentTimestamp);
            errLog.append(Constants.SPACE);
            errLog.append(erroneousRecord);
            errLog.append(Constants.SPACE);
            errLog.append(errorCode);
            errLog.append(Constants.SPACE);
            errLog.append(errorCause);

            Path logFilePath = FileSystems.getDefault().getPath(".", logFileName.toString());

            Files.write(logFilePath,
                    errLog.toString().getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

            Files.write(logFilePath,
                    Constants.NEXT_LINE.getBytes(),
                    StandardOpenOption.APPEND);
        } catch(IOException ioe) {
            logger.error("IOException while writing error log to file : " + logFileName + "Error : " + ioe.getMessage());
        }
        
    }

    @Override
    public void writeBulkUploadProcessingSummary(String logFileName,
            Integer numberOfSuccessfulRecords,
            Integer numberOfFailedRecords) {
        try {

            java.util.Date date = new java.util.Date();
            Timestamp currentTimestamp = new Timestamp(date.getTime());
            String logTitle = "BULK UPLOAD SUMMARY : ";
            
            StringBuffer errLog = new StringBuffer();
            errLog.append(logTitle);
            errLog.append(currentTimestamp);
            errLog.append(Constants.SPACE);
            errLog.append("Number Of Records Successful : ");
            errLog.append(numberOfSuccessfulRecords.toString());
            errLog.append("Number Of Records Failed : ");
            errLog.append(numberOfFailedRecords.toString());
            
            Path logFilePath = FileSystems.getDefault().getPath(".", logFileName.toString());
            
            Files.write(logFilePath,
                    errLog.toString().getBytes(),
                    StandardOpenOption.CREATE, 
                    StandardOpenOption.APPEND);
            
            Files.write(logFilePath,
                    Constants.NEXT_LINE.getBytes(),
                    StandardOpenOption.APPEND);
        } catch(IOException ioe) {
            logger.error("IOException while writing error log to file : " + logFileName + "Error : " + ioe.getMessage());
        }
    }
}
