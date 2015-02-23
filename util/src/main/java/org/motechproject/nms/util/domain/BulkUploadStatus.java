package org.motechproject.nms.util.domain;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.domain.MdsEntity;

/**
 * This class Models data for BulkUploadStatus records.
 */
@Entity
public class BulkUploadStatus extends MdsEntity {

    @Field
    private String bulkUploadFileName;

    @Field
    private Integer numberOfSuccessfulRecords;

    @Field
    private Integer numberOfFailedRecords;

    @Field
    private String logFileServerIp;

    @Field
    private String logFileName;

    public BulkUploadStatus() {
    }

    public BulkUploadStatus(String bulkUploadFileName, Integer numberOfFailedRecords, Integer numberOfSuccessfulRecords, String logFileName, String logFileServerIp) {
        this.bulkUploadFileName = bulkUploadFileName;
        this.numberOfSuccessfulRecords = numberOfSuccessfulRecords;
        this.numberOfFailedRecords = numberOfFailedRecords;
        this.logFileServerIp = logFileServerIp;
        this.logFileName = logFileName;
    }

    public String getBulkUploadFileName() {
        return bulkUploadFileName;
    }

    public void setBulkUploadFileName(String bulkUploadFileName) {
        this.bulkUploadFileName = bulkUploadFileName;
    }

    public Integer getNumberOfSuccessfulRecords() {
        return numberOfSuccessfulRecords;
    }

    public void setNumberOfSuccessfulRecords(Integer numberOfSuccessfulRecords) {
        this.numberOfSuccessfulRecords = numberOfSuccessfulRecords;
    }

    public Integer getNumberOfFailedRecords() {
        return numberOfFailedRecords;
    }

    public void setNumberOfFailedRecords(Integer numberOfFailedRecords) {
        this.numberOfFailedRecords = numberOfFailedRecords;
    }

    public String getLogFileServerIp() {
        return logFileServerIp;
    }

    public void setLogFileServerIp(String logFileServerIp) {
        this.logFileServerIp = logFileServerIp;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }
}
