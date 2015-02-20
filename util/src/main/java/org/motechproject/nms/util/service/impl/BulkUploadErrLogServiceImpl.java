package org.motechproject.nms.util.service.impl;

import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.domain.BulkUploadStatus;
import org.motechproject.nms.util.service.BulkUploadErrLogService;
import org.motechproject.nms.util.service.BulkUploadStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.file.*;
import java.util.Collections;
import java.util.Enumeration;

import static org.motechproject.nms.util.constants.Constants.*;
/**
 * Simple implementation of the {@link BulkUploadStatusService} interface.
 *
 * This class provides the api to log erroneous csv upload records in a log file.
 * Log file name is taken as input in this method and is kept at a known path
 * at local node.
 * This api logs the erroneous record details along with error code and
 * error description
 */
@Service("bulkUploadErrLogService")
public class BulkUploadErrLogServiceImpl implements BulkUploadErrLogService {

    @Autowired
    private BulkUploadStatusService bulkUploadStatusService;

    private Logger logger = LoggerFactory.getLogger(BulkUploadErrLogServiceImpl.class);

    /**
     * This method is used to write logs for erroneous records
     * found during csv/bulk upload.
     *
     * Error logs are written to a separate log file
     * Error Logs contain the following information:
     * 1. Timestamp
     * 2. Erroneous record details
     * 3. Error Code
     * 4. Error Description
     *
     * @param    logFileName  String containing the name of log file including path
     * @param    bulkUploadErrRecordDetails  String describing another coding guideline
     */
    @Override
    public void writeBulkUploadErrLog(String bulkUploadFileName, String logFileName, BulkUploadError bulkUploadErrRecordDetails) {

        Path logFilePath = FileSystems.getDefault().getPath(logFileName.toString());

        StringBuffer errLog = new StringBuffer();
        errLog.append(ERROR_LOG_TITLE);
        errLog.append(NEXT_LINE);
        errLog.append(bulkUploadErrRecordDetails.getRecordDetails());
        errLog.append(DELIMITER);
        errLog.append(bulkUploadErrRecordDetails.getErrorCategory());
        errLog.append(DELIMITER);
        errLog.append(bulkUploadErrRecordDetails.getErrorDescription());
        errLog.append(NEXT_LINE);

        try {
            Files.write(logFilePath, errLog.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ioe) {
            logger.error("IOException while writing error log to file : " + logFileName + "Error : " + ioe.getMessage());
        }
    }


    /**
     * This method is used to write summary of all the records after bulk upload is complete
     *
     * The bulk upload summary is written to the file after bulk upload is complete
     * The bulk upload summary contains:
     * 1. Number of Records Successfully uploaded
     * 2. Number of records failed to upload
     *
     * @param    logFileName  String containing name of log file including file path
     * @param    csvProcessingSummary  Number of records successfully uploaded during bulk upload
     */
    @Override
    public void writeBulkUploadProcessingSummary(String userName, String bulkUploadFileName, String logFileName, CsvProcessingSummary csvProcessingSummary) {

        //Formatting the content of bulk upload processing summary
        StringBuffer uploadProcessingSummary = new StringBuffer();
        uploadProcessingSummary.append(NEXT_LINE);
        uploadProcessingSummary.append(UNDERLINE);
        uploadProcessingSummary.append(NEXT_LINE);
        uploadProcessingSummary.append(BULK_UPLOAD_SUMMARY_TITLE);
        uploadProcessingSummary.append(NEXT_LINE);
        uploadProcessingSummary.append(UNDERLINE);
        uploadProcessingSummary.append(NEXT_LINE);
        uploadProcessingSummary.append(TAB);
        uploadProcessingSummary.append(USER_NAME_TITLE);
        uploadProcessingSummary.append(userName);
        uploadProcessingSummary.append(NEXT_LINE);
        uploadProcessingSummary.append(TAB);
        uploadProcessingSummary.append(SUCCESSFUL_RECORDS_TITLE);
        uploadProcessingSummary.append(csvProcessingSummary.getSuccessCount().toString());
        uploadProcessingSummary.append(NEXT_LINE);
        uploadProcessingSummary.append(TAB);
        uploadProcessingSummary.append(FAILED_RECORDS_TITLE);
        uploadProcessingSummary.append(csvProcessingSummary.getFailureCount().toString());
        uploadProcessingSummary.append(NEXT_LINE);

        Path logFilePath = FileSystems.getDefault().getPath(logFileName.toString());

        //writing bulk upload processing summary log to specified log file
        try {
            Files.write(logFilePath, uploadProcessingSummary.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ioe) {
            logger.error("IOException while writing error log to file : " + logFileName + "Error : " + ioe.getMessage());
        }

        //Determining the full path of log file including the file name
        Path logFileLocation = Paths.get(System.getProperty("user.dir"), "utils", logFileName.toString());

        //Creating bulk upload status record
        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        bulkUploadStatus.setCreator(userName);
        bulkUploadStatus.setBulkUploadFileName(bulkUploadFileName);
        bulkUploadStatus.setLogFileName(logFileLocation.toString());
        bulkUploadStatus.setNumberOfFailedRecords(csvProcessingSummary.getFailureCount());
        bulkUploadStatus.setNumberOfSuccessfulRecords(csvProcessingSummary.getSuccessCount());
        bulkUploadStatus.setLogFileServerIp(getLocalHostName());

        //Adding the record to bulk upload status table
        bulkUploadStatusService.add(bulkUploadStatus);

    }

    /**
     * This method is used to identify local host ip address
     *
     * It uses the NetworkInterface and InetAddress to identify
     * the hostname.
     *
     * @return The ip address(host name) of the system
     */
    private String getLocalHostName() {
        Enumeration<NetworkInterface> netInterfaces = null;

        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            logger.error("Socket Exception while retrieving host name. Error : " + e.getMessage());
            return null;
        }

        for (NetworkInterface netInterface : Collections.list(netInterfaces)) {
            netInterface.getInetAddresses();
            Enumeration<InetAddress> inetAddresses = netInterface.getInetAddresses();

            for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                if(!(inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress())) {
                    return inetAddress.getHostName();
                }
            }
        }
        return null;
    }
}