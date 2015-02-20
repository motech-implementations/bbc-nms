package org.motechproject.nms.util.service.impl;

import org.motechproject.nms.util.BulkUploadError;
import org.motechproject.nms.util.CsvProcessingSummary;
import org.motechproject.nms.util.constants.Constants;
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
import java.util.Date;
import java.util.Enumeration;

/**
 * Simple implementation of the {@link BulkUploadStatusService} interface.
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
    public void writeBulkUploadErrLog(String logFileName, BulkUploadError bulkUploadErrRecordDetails) {

        Path logFilePath = FileSystems.getDefault().getPath(logFileName.toString());

        StringBuffer errLog = new StringBuffer();
        errLog.append(Constants.ERROR_LOG_TITLE);
        errLog.append(Constants.NEXT_LINE);
        errLog.append(bulkUploadErrRecordDetails.getRecordDetails());
        errLog.append(Constants.DELIMITER);
        errLog.append(bulkUploadErrRecordDetails.getErrorCategory());
        errLog.append(Constants.DELIMITER);
        errLog.append(bulkUploadErrRecordDetails.getErrorDescription());
        errLog.append(Constants.NEXT_LINE);

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
    public void writeBulkUploadProcessingSummary(String userName, String logFileName, CsvProcessingSummary csvProcessingSummary) {

        Date date = new Date();
        Path logFilePath = FileSystems.getDefault().getPath(logFileName.toString());

        StringBuffer uploadProcessingSummary = new StringBuffer();
        uploadProcessingSummary.append(Constants.NEXT_LINE);
        uploadProcessingSummary.append(Constants.UNDERLINE);
        uploadProcessingSummary.append(Constants.NEXT_LINE);
        uploadProcessingSummary.append(Constants.BULK_UPLOAD_SUMMARY_TITLE);
        uploadProcessingSummary.append(Constants.NEXT_LINE);
        uploadProcessingSummary.append(Constants.UNDERLINE);
        uploadProcessingSummary.append(Constants.NEXT_LINE);
        uploadProcessingSummary.append(Constants.TAB);
        uploadProcessingSummary.append(Constants.USER_NAME_TITLE);
        uploadProcessingSummary.append(userName);
        uploadProcessingSummary.append(Constants.NEXT_LINE);
        uploadProcessingSummary.append(Constants.TAB);
        uploadProcessingSummary.append(Constants.SUCCESSFUL_RECORDS_TITLE);
        uploadProcessingSummary.append(csvProcessingSummary.getSuccessCount().toString());
        uploadProcessingSummary.append(Constants.NEXT_LINE);
        uploadProcessingSummary.append(Constants.TAB);
        uploadProcessingSummary.append(Constants.FAILED_RECORDS_TITLE);
        uploadProcessingSummary.append(csvProcessingSummary.getFailureCount().toString());
        uploadProcessingSummary.append(Constants.NEXT_LINE);

        try {
            Files.write(logFilePath, uploadProcessingSummary.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ioe) {
            logger.error("IOException while writing error log to file : " + logFileName + "Error : " + ioe.getMessage());
        }

        Path logFileLocation = Paths.get(System.getProperty("user.dir"), "utils", logFileName.toString());
        BulkUploadStatus bulkUploadStatus = new BulkUploadStatus();
        bulkUploadStatus.setCreator(userName);
        bulkUploadStatus.setLogFileLocation(logFileLocation.toString());
        bulkUploadStatus.setNumberOfFailedRecords(csvProcessingSummary.getFailureCount());
        bulkUploadStatus.setNumberOfSuccessfulRecords(csvProcessingSummary.getSuccessCount());
        bulkUploadStatus.setHostName(getLocalHostName());
        bulkUploadStatusService.add(bulkUploadStatus);

    }

    private String getLocalHostName() {
        Enumeration<NetworkInterface> netInterfaces = null;

        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            //Add logger
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