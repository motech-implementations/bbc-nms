package bulkuploadloggingutil.impl;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.logging.Logger;

import bulkuploadloggingutil.BulkUploadErrLogService;

public class BulkUploadErrLogServiceImpl implements BulkUploadErrLogService{

    private Logger logger = LoggerFactory.getLogger(BulkUploadErrLogServiceImpl.class);

    @Override
    public void writeBulkUploadErrLog(String logFileName,
            String erroneousRecord, String errorCode, String errorCause) {
        
        try {
            
            String lineBreak = "\n"; //will remove hardcoding
            String blankSpace = " "; //will remove hardcoding
            java.util.Date date= new java.util.Date();
            Timestamp currentTimestamp =  new Timestamp(date.getTime());
            String logTitle = "ERROR :";
            
            StringBuffer errLog = new StringBuffer();
            errLog.append(logTitle);
            errLog.append(blankSpace);
            errLog.append(currentTimestamp);
            errLog.append(blankSpace);
            errLog.append(erroneousRecord);
            errLog.append(blankSpace);
            errLog.append(errorCode);
            errLog.append(blankSpace);
            errLog.append(errorCause);
            
            Path logFilePath = FileSystems.getDefault().getPath(".", logFileName.toString());
            
            Files.write(logFilePath,
                    errLog.toString().getBytes(),
                    StandardOpenOption.CREATE, 
                    StandardOpenOption.APPEND);
            
            Files.write(logFilePath,
                    lineBreak.getBytes(),
                    StandardOpenOption.APPEND);
        }
        catch ( IOException ioe ) {
            logger.error("IOException while writing error log to file : " + logFileName + "Error : " + ioe.getMessage());
        }
        
    }

    @Override
    public void writeBulkUploadProcessingSummary(String logFileName,
            Integer numberOfSuccessfulRecords,
            Integer numberOfFailedRecords) {
        try {
            
            String lineBreak = "\n"; //will remove hardcoding
            String blankSpace = " "; //will remove hardcoding
            java.util.Date date= new java.util.Date();
            Timestamp currentTimestamp =  new Timestamp(date.getTime());
            String logTitle = "BULK UPLOAD SUMMARY : ";
            
            StringBuffer errLog = new StringBuffer();
            errLog.append(logTitle);
            errLog.append(currentTimestamp);
            errLog.append(blankSpace);
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
                    lineBreak.getBytes(),
                    StandardOpenOption.APPEND);
        }
        catch ( IOException ioe ) {
            logger.error("IOException while writing error log to file : " + logFileName + "Error : " + ioe.getMessage());
        }
        

        
    }


}
