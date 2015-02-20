package org.motechproject.nms.util.domain;

import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * Models data for simple records in a portable manner.
 */
public class BulkUploadStatus extends MdsEntity{

    @Field
    private String logFileLocation;

    @Field
    private String hostName;

    @Field
    private Integer numberOfFailedRecords;

    @Field
    private Integer numberOfSuccessfulRecords;

    public BulkUploadStatus() {
    }

    public BulkUploadStatus(String hostName, String logFileLocation, Integer numberOfFailedRecords, Integer numberOfSuccessfulRecords) {
        this.hostName = hostName;
        this.logFileLocation = logFileLocation;
        this.numberOfFailedRecords = numberOfFailedRecords;
        this.numberOfSuccessfulRecords = numberOfSuccessfulRecords;
    }

    public Integer getNumberOfFailedRecords() {
        return numberOfFailedRecords;
    }

    public void setNumberOfFailedRecords(Integer numberOfFailedRecords) {
        this.numberOfFailedRecords = numberOfFailedRecords;
    }

    public String getLogFileLocation() {
        return logFileLocation;
    }

    public void setLogFileLocation(String logFileLocation) {
        this.logFileLocation = logFileLocation;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getNumberOfSuccessfulRecords() {
        return numberOfSuccessfulRecords;
    }

    public void setNumberOfSuccessfulRecords(Integer numberOfSuccessfulRecords) {
        this.numberOfSuccessfulRecords = numberOfSuccessfulRecords;
    }

}
